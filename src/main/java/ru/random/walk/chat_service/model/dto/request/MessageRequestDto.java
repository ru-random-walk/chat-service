package ru.random.walk.chat_service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;
import ru.random.walk.chat_service.model.dto.response.message.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MessageRequestDto(
        PayloadDto payloadDto,
        Type type,
        UUID chatId,
        @Schema(example = "18:00 22-09-2024")
        @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
        LocalDateTime createdAt
) {
}
