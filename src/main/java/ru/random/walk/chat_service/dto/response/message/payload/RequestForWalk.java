package ru.random.walk.chat_service.dto.response.message.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.dto.response.message.Payload;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
public class RequestForWalk extends Payload {
    private final Location location;
    @Schema(example = "18:00 22-09-2024")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm dd-MM-yyyy")
    @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
    private final LocalDateTime startsAt;
    @Nullable
    private final Boolean answer;

    public RequestForWalk(String type, Location location, LocalDateTime startsAt, @Nullable Boolean answer) {
        super(type);
        this.location = location;
        this.startsAt = startsAt;
        this.answer = answer;
    }
}
