package ru.random.walk.chat_service.dto.message;

import java.sql.Timestamp;
import java.util.UUID;

public record Message(
        UUID id,
        Payload payload,
        Type type,
        UUID chatId,
        String sender,
        String recipient,
        boolean markedAsRead,
        Timestamp createdAt
) {
}
