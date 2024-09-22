package ru.random.walk.chat_service.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestForWalk extends Payload {
    private final Location location;
    private final Timestamp startsAt;
    @Nullable
    private final Boolean answer;
}
