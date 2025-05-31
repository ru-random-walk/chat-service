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
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.domain.payload.TextPayload;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.UserEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.repository.UserRepository;
import ru.random.walk.dto.SendNotificationEvent;
import ru.random.walk.topic.EventTopic;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
                .fullName("Gore")
                .build());
        var recipient = userRepository.saveAndFlush(UserEntity.builder()
                .id(UUID.randomUUID())
                .fullName("Chuhla")
                .build());

        // Отправляем сообщение - привет
        var messagePayload = new TextPayload("Hi!");
        messageService.sendMessage(MessageEntity.builder()
                .sender(sender.getId())
                .chatId(chat.getId())
                .recipient(recipient.getId())
                .payload(messagePayload)
                .build());

        // Проверяем что уведомление было отправлено в нужном формате
        verify(kafkaTemplate, times(1))
                .send(
                        eq(EventTopic.SEND_NOTIFICATION),
                        jsonEq(objectMapper.writeValueAsString(
                                SendNotificationEvent.builder()
                                        .title("New message! From Gore")
                                        .userId(recipient.getId())
                                        .additionalData(Map.of(
                                                "sender", sender.getId().toString(),
                                                "chatId", chat.getId().toString()
                                        ))
                                        .body(objectMapper.writeValueAsString(messagePayload))
                                        .build()
                        ))
                );
    }
}