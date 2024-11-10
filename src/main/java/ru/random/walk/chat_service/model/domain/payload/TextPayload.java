package ru.random.walk.chat_service.model.domain.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextPayload extends MessagePayload {
    private String text;

    @SuppressWarnings("unused")
    public TextPayload() {
        super("text");
    }

    protected TextPayload(String text, String type) {
        super(type);
        this.text = text;
    }

    public TextPayload(String text) {
        this(text, "text");
    }
}
