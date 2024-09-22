package ru.random.walk.chat_service.dto.filter;

import java.sql.Timestamp;

public record MessageFilter(
        String message,
        Timestamp from,
        Timestamp to
) {
}
