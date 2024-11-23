package ru.random.walk.chat_service.model.domain;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public record MessageFilter(
        @Nullable String message,
        @Nullable LocalDateTime from,
        @Nullable LocalDateTime to
) {
}
