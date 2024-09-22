package ru.random.walk.chat_service.dto.pagination;

public record PageRequest(
        long number,
        long size,
        long total
) {
}
