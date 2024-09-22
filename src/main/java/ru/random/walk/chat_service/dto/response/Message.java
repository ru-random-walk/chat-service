package ru.random.walk.chat_service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.dto.response.message.Payload;
import ru.random.walk.chat_service.dto.response.message.Type;

import java.time.LocalDateTime;
import java.util.UUID;

public record Message(
        UUID id,
        Payload payload,
        Type type,
        UUID chatId,
        String sender,
        String recipient,
        boolean markedAsRead,
        @Schema(example = "18:00 22-09-2024")
        @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
        LocalDateTime createdAt
) {
}
