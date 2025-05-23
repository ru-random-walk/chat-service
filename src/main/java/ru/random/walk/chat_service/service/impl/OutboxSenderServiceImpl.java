package ru.random.walk.chat_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.model.entity.OutboxMessage;
import ru.random.walk.chat_service.repository.OutboxRepository;
import ru.random.walk.chat_service.service.OutboxSenderService;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxSenderServiceImpl implements OutboxSenderService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void sendMessage(String topic, Object payload, Map<String, String> additionalInfo) {
        try {
            var message = new OutboxMessage();
            message.setTopic(topic);
            message.setPayload(objectMapper.writeValueAsString(payload));
            message.setAdditionalInfo(additionalInfo);
            outboxRepository.save(message);
        } catch (Exception e) {
            log.error("Error saving outbox message for topic {} with payload {}", topic, payload, e);
            throw new RuntimeException(e);
        }
    }
}
