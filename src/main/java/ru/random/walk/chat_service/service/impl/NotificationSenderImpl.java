package ru.random.walk.chat_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
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
            String fromTitlePart = userRepository.findById(message.getSender())
                    .map(UserEntity::getFullName)
                    .map("From %s"::formatted)
                    .orElse("");
            var messageBody = objectMapper.writeValueAsString(message.getPayload());
            var notification = SendNotificationEvent.builder()
                    .title("New message! %s".formatted(fromTitlePart))
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
}
