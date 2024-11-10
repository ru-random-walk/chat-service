package ru.random.walk.chat_service.model.dto.response.message.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
public class RequestForWalkDto extends PayloadDto {
    private final LocationDto locationDto;
    @Schema(example = "18:00 22-09-2024")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "hh:mm dd-MM-yyyy")
    @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
    private final LocalDateTime startsAt;
    @Nullable
    private final Boolean answer;

    protected RequestForWalkDto(String type, LocationDto locationDto, LocalDateTime startsAt, @Nullable Boolean answer) {
        super(type);
        this.locationDto = locationDto;
        this.startsAt = startsAt;
        this.answer = answer;
    }

    public RequestForWalkDto(LocationDto locationDto, LocalDateTime startsAt, @Nullable Boolean answer) {
        this("request_for_walk", locationDto, startsAt, answer);
    }
}
