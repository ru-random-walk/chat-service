package ru.random.walk.chat_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.domain.payload.MessagePayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.domain.payload.TextPayload;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.UserEntity;
import ru.random.walk.chat_service.repository.UserRepository;
import ru.random.walk.chat_service.service.NotificationSender;
import ru.random.walk.dto.SendNotificationEvent;
import ru.random.walk.topic.EventTopic;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class NotificationSenderImpl implements NotificationSender {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void notifyAboutNewMessage(MessageEntity message) {
        try {
            String fromPart = userRepository.findById(message.getSender())
                    .map(UserEntity::getFullName)
                    .map("от %s"::formatted)
                    .orElse("");
            String messageBody = parseMessageBody(message.getPayload(), fromPart);
            var notification = SendNotificationEvent.builder()
                    .title("Новое сообщение %s!".formatted(fromPart))
                    .body(messageBody)
                    .userId(message.getRecipient())
                    .additionalData(Map.of(
                            "sender", message.getSender().toString(),
                            "chatId", message.getChatId().toString()
                    ))
                    .build();
            kafkaTemplate.send(EventTopic.SEND_NOTIFICATION, objectMapper.writeValueAsString(notification));
        } catch (Exception e) {
            log.warn(
                    "Failed to notify user: [{}] about message from: [{}]",
                    message.getRecipient(),
                    message.getSender(),
                    e
            );
        }
    }

    private static String parseMessageBody(MessagePayload payload, String fromPart) {
        return switch (payload) {
            case TextPayload text -> text.getText();
            case RequestForWalkPayload requestForWalk ->
                    "Приглашение на прогулку %s! В городе %s на улице %s!".formatted(
                            fromPart,
                            requestForWalk.getLocation().getCity(),
                            requestForWalk.getLocation().getStreet()
                    );
            default -> throw new IllegalStateException("Unexpected value: " + payload);
        };
    }
}
