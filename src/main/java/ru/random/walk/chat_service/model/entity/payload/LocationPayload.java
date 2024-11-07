package ru.random.walk.chat_service.model.entity.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationPayload extends MessagePayload {
    private final double longitude;
    private final double latitude;

    public LocationPayload(String type, double longitude, double latitude) {
        super(type);
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @SuppressWarnings("unused")
    public LocationPayload(double longitude, double latitude) {
        this("location", longitude, latitude);
    }
}
