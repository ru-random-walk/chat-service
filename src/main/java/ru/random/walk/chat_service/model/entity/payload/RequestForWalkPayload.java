package ru.random.walk.chat_service.model.entity.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RequestForWalkPayload extends MessagePayload {
    private final LocationPayload location;
    private final LocalDateTime startsAt;
    private final Boolean answer;

    public RequestForWalkPayload(String type, LocationPayload location, LocalDateTime startsAt, Boolean answer) {
        super(type);
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    @SuppressWarnings("unused")
    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt, Boolean answer) {
        this("requestForWalk", location, startsAt, answer);
    }
}
