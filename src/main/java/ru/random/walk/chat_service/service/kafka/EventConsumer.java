package ru.random.walk.chat_service.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.service.ChatService;
import ru.random.walk.dto.CreatePrivateChatEvent;
import ru.random.walk.kafka.EventTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final ChatService chatService;

    @KafkaListener(topics = EventTopic.CREATE_CHAT)
    public void listen(CreatePrivateChatEvent event) {
        log.info("Received event: {}", event);
        chatService.create(event);
    }
}