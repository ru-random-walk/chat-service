package ru.random.walk.chat_service.dto.chat;

import ru.random.walk.chat_service.dto.filter.ChatFilter;
import ru.random.walk.chat_service.dto.pagination.PageRequest;

public record ChatPageRequest(
        PageRequest page,
        ChatFilter filter
) {
}
