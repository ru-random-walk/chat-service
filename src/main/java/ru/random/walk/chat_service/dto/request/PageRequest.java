package ru.random.walk.chat_service.dto.request;

public record PageRequest(
        long number,
        long size
) {
}
