package ru.random.walk.chat_service.dto.response.message.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.dto.response.message.Payload;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestForWalk extends Payload {
    private final Location location;
    @Schema(example = "18:00 22-09-2024")
    @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
    private final LocalDateTime startsAt;
    @Nullable
    private final Boolean answer;
}