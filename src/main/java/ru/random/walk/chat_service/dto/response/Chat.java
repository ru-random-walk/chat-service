package ru.random.walk.chat_service.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Chat(
        UUID id
) {
}
