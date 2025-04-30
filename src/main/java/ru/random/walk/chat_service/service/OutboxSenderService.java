package ru.random.walk.chat_service.service;

import java.util.Map;

public interface OutboxSenderService {
    void sendMessage(String topic, Object payload, Map<String, String> additionalInfo);

    default void sendMessage(String topic, Object payload) {
        sendMessage(topic, payload, Map.of());
    }

    default void sendMessage(Enum<?> enam, Object payload, Map<String, String> additionalInfo) {
        sendMessage(enam.name(), payload, additionalInfo);
    }

    default void sendMessage(Enum<?> enam, Object payload) {
        sendMessage(enam.name(), payload);
    }
}
