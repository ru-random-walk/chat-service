package ru.random.walk.chat_service.model.domain.payload;

import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RequestForWalkPayload extends MessagePayload {
    private LocationPayload location;
    private LocalDateTime startsAt;
    @Nullable
    private Boolean answer;

    @SuppressWarnings("unused")
    public RequestForWalkPayload(){
        super("request_for_walk");
    }

    protected RequestForWalkPayload(String type, LocationPayload location, LocalDateTime startsAt, @Nullable Boolean answer) {
        super(type);
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    @SuppressWarnings("unused")
    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt, Boolean answer) {
        this("request_for_walk", location, startsAt, answer);
    }

    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt) {
        this("request_for_walk", location, startsAt, null);
    }
}
