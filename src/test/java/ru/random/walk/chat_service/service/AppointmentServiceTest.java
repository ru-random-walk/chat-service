package ru.random.walk.chat_service.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.dto.matcher.AppointmentDetailsDto;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.AppointmentRepository;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.service.client.MatcherClient;
import ru.random.walk.chat_service.service.job.OutboxSendingJob;
import ru.random.walk.dto.RequestedAppointmentStateEvent;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AppointmentServiceTest extends AbstractContainerTest {
    private final AppointmentService appointmentService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final OutboxSendingJob outboxSendingJob;
    private final AppointmentRepository appointmentRepository;

    @MockBean
    private MatcherClient matcherClient;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    void testUpdateState() {
        // Был какой-то чат
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        // Потом в этот чат отправили сообщение - запрос на прогулку
        var message = MessageEntity.builder()
                .sender(UUID.randomUUID())
                .recipient(UUID.randomUUID())
                .chatId(chat.getId())
                .payload(new RequestForWalkPayload(
                        new LocationPayload(0d, 0d, "Semyonov", "Sportivnaya", null),
                        OffsetDateTime.now().plusMinutes(1)
                ))
                .build();
        var appointmentId = UUID.randomUUID();
        when(matcherClient.requestForAppointment(any()))
                .thenReturn(AppointmentDetailsDto.builder()
                        .id(appointmentId)
                        .build());
        messageService.sendMessage(message);
        outboxSendingJob.execute(null);

        // Далее матчер прислал ивент о принятии приглашения
        appointmentService.updateState(RequestedAppointmentStateEvent.builder()
                .appointmentId(appointmentId)
                .isAccepted(true)
                .build());

        // В итоге в сообщении должен быть ответ с принятым приглашением
        message = messageRepository.findById(message.getId()).orElseThrow();
        var requestForWalkPayload = assertInstanceOf(RequestForWalkPayload.class, message.getPayload());
        assertEquals(Boolean.TRUE, requestForWalkPayload.getAnswer());
    }
}