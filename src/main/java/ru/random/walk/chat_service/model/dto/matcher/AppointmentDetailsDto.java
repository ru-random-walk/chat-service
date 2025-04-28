package ru.random.walk.chat_service.model.dto.matcher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @see ru.random.walk.chat_service.model.dto.matcher.AppointmentStatus AppointmentStatus enum to convert raw status in it
 */
@Builder
public record AppointmentDetailsDto(
        UUID id,
        List<UUID> participants,
        @Schema(example = "2024-10-31T01:30:00.000+03:00")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime startsAt,
        @Schema(example = "2024-10-31T01:30:00.000+03:00")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime updatedAt,
        @Schema(example = "2024-10-31T01:30:00.000+03:00")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime endedAt,
        String status,
        Double longitude,
        Double latitude
) {
}
