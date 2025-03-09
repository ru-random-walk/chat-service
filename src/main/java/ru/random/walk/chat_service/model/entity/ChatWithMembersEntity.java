package ru.random.walk.chat_service.model.entity;

import java.util.UUID;

public record ChatWithMembersEntity(
        UUID chatId,
        UUID[] memberIds
) {
}
