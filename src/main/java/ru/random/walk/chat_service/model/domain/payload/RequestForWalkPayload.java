package ru.random.walk.chat_service.model.domain.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RequestForWalkPayload extends MessagePayload {
    private LocationPayload location;
    private LocalDateTime startsAt;
    @Nullable
    private Boolean answer;

    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt, @Nullable Boolean answer) {
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt) {
        this(location, startsAt, null);
    }
}
