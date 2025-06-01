package ru.random.walk.chat_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.domain.payload.TextPayload;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.UserEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.model.exception.ValidationException;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.repository.UserRepository;
import ru.random.walk.chat_service.util.StubDataUtil;
import ru.random.walk.dto.SendNotificationEvent;
import ru.random.walk.topic.EventTopic;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static ru.random.walk.chat_service.mockito.JsonArgMatcher.jsonEq;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class MessageServiceTest extends AbstractContainerTest {
    private final MessageService messageService;

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @SpyBean
    private final KafkaTemplate<String, String> kafkaTemplate;
    @SpyBean
    private final SimpUserRegistry userRegistry;
    @MockBean
    private final SimpMessagingTemplate messagingTemplate;

    @Test
    void testSendingMessageWithNotification() throws JsonProcessingException {
        // Был какой-то чат
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        // Отправитель и получатель сообщения
        var sender = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Чумындра")
                .build());
        var recipient = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Пачко")
                .build());

        // Отправляем сообщение - привет
        var textPayload = new TextPayload("Приветик!");
        messageService.sendMessage(MessageEntity.builder()
                .sender(sender.getId())
                .chatId(chat.getId())
                .recipient(recipient.getId())
                .payload(textPayload)
                .build());

        // Проверяем что уведомление о текстовом сообщении было отправлено в нужном формате
        verify(kafkaTemplate)
                .send(
                        eq(EventTopic.SEND_NOTIFICATION),
                        jsonEq(objectMapper.writeValueAsString(
                                SendNotificationEvent.builder()
                                        .title("Новое сообщение от Чумындра!")
                                        .userId(recipient.getId())
                                        .additionalData(Map.of(
                                                "sender", sender.getId().toString(),
                                                "chatId", chat.getId().toString()
                                        ))
                                        .body("Приветик!")
                                        .build()
                        ))
                );
    }

    @Test
    void testSendingRequestForWalkMessageWithNotification() throws JsonProcessingException {
        // Был какой-то чат
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        // Отправитель и получатель сообщения
        var sender = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Чумындра")
                .build());
        var recipient = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Пачко")
                .build());

        // Отправляем приглашение на прогулку
        var requestForWalk = new RequestForWalkPayload(
                new LocationPayload(0, 0, "Москва", "Льва Толстого", null),
                LocalDateTime.now().plusMinutes(1)
        );
        messageService.sendMessage(MessageEntity.builder()
                .sender(sender.getId())
                .chatId(chat.getId())
                .recipient(recipient.getId())
                .payload(requestForWalk)
                .build());

        // Проверяем что уведомление о приглашении на прогулку было отправлено в нужном формате
        verify(kafkaTemplate)
                .send(
                        eq(EventTopic.SEND_NOTIFICATION),
                        jsonEq(objectMapper.writeValueAsString(
                                SendNotificationEvent.builder()
                                        .title("Новое сообщение от Чумындра!")
                                        .userId(recipient.getId())
                                        .additionalData(Map.of(
                                                "sender", sender.getId().toString(),
                                                "chatId", chat.getId().toString()
                                        ))
                                        .body("""
                                                Приглашение на прогулку от Чумындра! \
                                                В городе Москва на улице Льва Толстого!
                                                """.strip())
                                        .build()
                        ))
                );
    }

    @Test
    void testSendingRequestForWalkMessageValidationFailed() {
        // Был какой-то чат
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        // Отправитель и получатель сообщения
        var sender = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Чумындра")
                .build());
        var recipient = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Пачко")
                .build());

        // Отправляем приглашение на прогулку с временем старта в прошлом
        var requestForWalk = new RequestForWalkPayload(
                new LocationPayload(0, 0, "Москва", "Льва Толстого", null),
                LocalDateTime.now().minusDays(1).minusHours(23)
        );
        assertThrows(
                ValidationException.class,
                () -> messageService.sendMessage(MessageEntity.builder()
                        .sender(sender.getId())
                        .chatId(chat.getId())
                        .recipient(recipient.getId())
                        .payload(requestForWalk)
                        .build())
        );
        verifyNoInteractions(messagingTemplate);
    }

    @Test
    void testSendingMessageWithoutNotificationDueToConnectedUser() throws JsonProcessingException {
        // Был какой-то чат
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        // Отправитель и получатель сообщения
        var sender = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Чумындра")
                .build());
        var recipient = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Пачко")
                .build());

        when(userRegistry.getUser(recipient.getId().toString()))
                .thenReturn(new StubDataUtil.SimpUserStub());

        // Отправляем приглашение на прогулку
        var requestForWalk = new RequestForWalkPayload(
                new LocationPayload(0, 0, "Москва", "Льва Толстого", null),
                LocalDateTime.now().plusMinutes(1)
        );
        messageService.sendMessage(MessageEntity.builder()
                .sender(sender.getId())
                .chatId(chat.getId())
                .recipient(recipient.getId())
                .payload(requestForWalk)
                .build());

        // Проверяем что уведомление о приглашении на прогулку Не было отправлено в нужном формате
        verify(kafkaTemplate, times(0))
                .send(
                        eq(EventTopic.SEND_NOTIFICATION),
                        jsonEq(objectMapper.writeValueAsString(
                                SendNotificationEvent.builder()
                                        .title("Новое сообщение от Чумындра!")
                                        .userId(recipient.getId())
                                        .additionalData(Map.of(
                                                "sender", sender.getId().toString(),
                                                "chatId", chat.getId().toString()
                                        ))
                                        .body("""
                                                Приглашение на прогулку от Чумындра! \
                                                В городе Москва на улице Льва Толстого!
                                                """.strip())
                                        .build()
                        ))
                );
    }
}