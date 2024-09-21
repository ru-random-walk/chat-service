package ru.random.walk.chat_service.model.message;

import ru.random.walk.chat_service.model.filter.MessageFilter;
import ru.random.walk.chat_service.model.pagination.PageRequest;

public record MessagePageRequest(
        PageRequest page,
        MessageFilter filter
) {
}
