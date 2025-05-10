package ru.random.walk.chat_service.model.domain.event;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AppointmentCreatedForRequestForWalkEvent(
        UUID appointmentId,
        UUID messageId
) {
}
