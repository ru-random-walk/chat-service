package ru.random.walk.chat_service.model.dto.response.message.payload;

import lombok.Getter;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;

@Getter
public class LocationDto extends PayloadDto {
    private final double longitude;
    private final double latitude;

    public LocationDto(String type, double longitude, double latitude) {
        super(type);
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
