package ru.random.walk.chat_service.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;
import ru.random.walk.chat_service.model.dto.response.message.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MessageDto(
        UUID id,
        PayloadDto payloadDto,
        Type type,
        UUID chatId,
        boolean markedAsRead,
        @Schema(example = "18:00 22-09-2024")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm dd-MM-yyyy")
        @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
        LocalDateTime createdAt
) {
}
