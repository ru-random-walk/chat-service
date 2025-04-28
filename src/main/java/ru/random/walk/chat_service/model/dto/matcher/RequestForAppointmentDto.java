package ru.random.walk.chat_service.model.dto.matcher;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RequestForAppointmentDto(
        UUID requesterId,
        UUID partnerId,
        OffsetDateTime startTime,
        Double longitude,
        Double latitude
) {
}
