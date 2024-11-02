package ru.random.walk.chat_service.model.dto.response.message.payload;

import lombok.Getter;
import ru.random.walk.chat_service.model.dto.response.message.Payload;

@Getter
public class Location extends Payload {
    private final double longitude;
    private final double latitude;

    public Location(String type, double longitude, double latitude) {
        super(type);
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
