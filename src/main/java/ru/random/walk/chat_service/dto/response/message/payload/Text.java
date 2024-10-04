package ru.random.walk.chat_service.dto.response.message.payload;

import lombok.Getter;
import ru.random.walk.chat_service.dto.response.message.Payload;

@Getter
public class Text extends Payload {
    private final String text;

    public Text(String text) {
        super("text");
        this.text = text;
    }

    @SuppressWarnings("unused")
    public Text(String text, String type) {
        super(type);
        this.text = text;
    }
}
