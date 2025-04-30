package ru.random.walk.chat_service.service;

public interface OutboxSenderService {
    void sendMessage(String topic, Object payload);

    default void sendMessage(Enum<?> enam, Object payload) {
        sendMessage(enam.name(), payload);
    }
}
