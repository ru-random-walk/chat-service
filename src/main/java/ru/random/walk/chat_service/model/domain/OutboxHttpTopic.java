package ru.random.walk.chat_service.model.domain;

import java.util.Optional;

public enum OutboxHttpTopic {
    SEND_CREATING_APPOINTMENT_TO_MATCHER;

    public static Optional<OutboxHttpTopic> getValueOf(String value) {
        try {
            return Optional.of(OutboxHttpTopic.valueOf(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
