package ru.random.walk.chat_service.model.filter;

import java.sql.Timestamp;

public record MessageFilter(
        String message,
        Timestamp from,
        Timestamp to
) {
}
