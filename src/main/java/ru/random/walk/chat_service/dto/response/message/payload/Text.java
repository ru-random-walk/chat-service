package ru.random.walk.chat_service.dto.response.message.payload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.random.walk.chat_service.dto.response.message.Payload;

@EqualsAndHashCode(callSuper = true)
@Data
public class Text extends Payload {
    private final String text;
}
