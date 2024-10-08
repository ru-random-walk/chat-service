package ru.random.walk.chat_service.dto.response;

import java.util.List;

public record Page<T>(
        List<T> list,
        long number,
        long size,
        long total
) {
}
