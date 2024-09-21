package ru.random.walk.chat_service.model.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Text extends Payload {
    private final String text;
}
