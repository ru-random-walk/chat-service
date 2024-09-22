package ru.random.walk.chat_service.dto.message;

import ru.random.walk.chat_service.dto.filter.MessageFilter;
import ru.random.walk.chat_service.dto.pagination.PageRequest;

public record MessagePageRequest(
        PageRequest page,
        MessageFilter filter
) {
}
