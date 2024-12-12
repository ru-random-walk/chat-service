package ru.random.walk.chat_service.model.domain.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationPayload extends MessagePayload {
    private double longitude;
    private double latitude;
}
