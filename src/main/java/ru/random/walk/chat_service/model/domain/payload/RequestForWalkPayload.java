package ru.random.walk.chat_service.model.domain.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.random.walk.chat_service.controller.format.GlobalDateTimeFormat;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class RequestForWalkPayload extends MessagePayload {
    private LocationPayload location;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = GlobalDateTimeFormat.DEFAULT_PATTERN)
    private LocalDateTime startsAt;
    @Nullable
    private Boolean answer;
    @Nullable
    private UUID appointmentId;

    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt, @Nullable Boolean answer) {
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    public RequestForWalkPayload(LocationPayload location, LocalDateTime startsAt) {
        this(location, startsAt, null);
    }
}
