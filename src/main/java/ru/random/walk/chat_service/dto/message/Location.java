package ru.random.walk.chat_service.dto.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Location extends Payload {
    private final double longitude;
    private final double latitude;
}
