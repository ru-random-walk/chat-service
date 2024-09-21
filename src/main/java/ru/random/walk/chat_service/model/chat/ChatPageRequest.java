package ru.random.walk.chat_service.model.chat;

import ru.random.walk.chat_service.model.filter.ChatFilter;
import ru.random.walk.chat_service.model.pagination.PageRequest;

public record ChatPageRequest(
        PageRequest page,
        ChatFilter filter
) {
}
