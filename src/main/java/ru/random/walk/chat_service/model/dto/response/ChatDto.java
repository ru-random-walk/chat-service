package ru.random.walk.chat_service.model.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ChatDto(
        UUID id,
        List<UUID> memberIds
) {
    public ChatDto(UUID id, UUID[] memberIds) {
        this(id, List.of(memberIds));
    }
}
