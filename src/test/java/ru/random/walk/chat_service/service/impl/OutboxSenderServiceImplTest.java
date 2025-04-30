package ru.random.walk.chat_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.AbstractPostgresContainerTest;
import ru.random.walk.chat_service.model.domain.OutboxAdditionalInfoKey;
import ru.random.walk.chat_service.model.domain.OutboxHttpTopic;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.dto.matcher.AppointmentDetailsDto;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.OutboxMessage;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.repository.OutboxRepository;
import ru.random.walk.chat_service.service.OutboxSenderService;
import ru.random.walk.chat_service.service.client.MatcherClient;
import ru.random.walk.chat_service.service.job.OutboxSendingJob;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class OutboxSenderServiceImplTest extends AbstractPostgresContainerTest {
    private final OutboxSenderService outboxSenderService;
    private final OutboxSendingJob outboxSendingJob;
    private final OutboxRepository outboxRepository;
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ObjectMapper objectMapper;

    @MockBean
    private MatcherClient matcherClient;

    @Test
    @Transactional
    @Rollback
    void sendMessageSavesToDb() {
        var dto = RequestForAppointmentDto.builder()
                .requesterId(UUID.randomUUID())
                .partnerId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .longitude(0d)
                .latitude(0d)
                .build();
        outboxSenderService.sendMessage(OutboxHttpTopic.SEND_CREATING_APPOINTMENT_TO_MATCHER, dto);
        var messages = outboxRepository.findAll();

        assertFalse(messages.isEmpty());
        assertEquals(OutboxHttpTopic.SEND_CREATING_APPOINTMENT_TO_MATCHER.name(), messages.getLast().getTopic());
        assertDoesNotThrow(() -> objectMapper.readValue(messages.getLast().getPayload(), RequestForAppointmentDto.class));
        assertNotNull(messages.getLast().getCreatedAt());
    }

    @Test
    @Transactional
    @Rollback
    void checkOutboxJobIsSendingMessages() throws JsonProcessingException {
        when(matcherClient.requestForAppointment(any()))
                .thenReturn(AppointmentDetailsDto.builder()
                        .id(UUID.randomUUID())
                        .participants(List.of(UUID.randomUUID(), UUID.randomUUID()))
                        .startsAt(OffsetDateTime.now())
                        .updatedAt(OffsetDateTime.now())
                        .endedAt(OffsetDateTime.now())
                        .status("Some raw status")
                        .longitude(0d)
                        .latitude(0d)
                        .build());

        var dto = RequestForAppointmentDto.builder()
                .requesterId(UUID.randomUUID())
                .partnerId(UUID.randomUUID())
                .startTime(OffsetDateTime.now())
                .longitude(0d)
                .latitude(0d)
                .build();
        String payload = objectMapper.writeValueAsString(dto);

        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        var message = messageRepository.save(MessageEntity.builder()
                .chatId(chat.getId())
                .recipient(UUID.randomUUID())
                .sender(UUID.randomUUID())
                .sentAt(LocalDateTime.now())
                .payload(new RequestForWalkPayload(
                        new LocationPayload(0d, 0d, "Semyonov", "Sportivnaya", null),
                        LocalDateTime.now()
                ))
                .build());

        OutboxMessage outboxMessage = new OutboxMessage();
        outboxMessage.setPayload(payload);
        outboxMessage.setTopic(OutboxHttpTopic.SEND_CREATING_APPOINTMENT_TO_MATCHER.name());
        outboxMessage.setAdditionalInfo(Map.of(
                OutboxAdditionalInfoKey.MESSAGE_ID.name(), message.getId().toString()
        ));

        assertFalse(outboxMessage.isSent());

        outboxMessage = outboxRepository.save(outboxMessage);

        outboxSendingJob.execute(null);

        var outboxResult = outboxRepository.findById(outboxMessage.getId()).orElseThrow();
        assertTrue(outboxResult.isSent());
    }
}