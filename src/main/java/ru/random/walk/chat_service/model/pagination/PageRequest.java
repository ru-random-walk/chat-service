package ru.random.walk.chat_service.model.pagination;

public record PageRequest(
        long number,
        long size,
        long total
) {
}
