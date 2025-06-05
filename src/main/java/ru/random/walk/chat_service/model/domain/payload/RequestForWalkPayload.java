package ru.random.walk.chat_service.model.domain.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.controller.format.GlobalDateTimeFormat;

import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class RequestForWalkPayload extends MessagePayload {
    private LocationPayload location;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startsAt;
    @Nullable
    private Boolean answer;
    @Nullable
    private UUID appointmentId;

    public RequestForWalkPayload(LocationPayload location, OffsetDateTime startsAt, @Nullable Boolean answer) {
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    public RequestForWalkPayload(LocationPayload location, OffsetDateTime startsAt) {
        this(location, startsAt, null);
    }
}
