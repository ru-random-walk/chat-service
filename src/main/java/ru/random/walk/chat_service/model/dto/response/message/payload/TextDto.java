package ru.random.walk.chat_service.model.dto.response.message.payload;

import lombok.Getter;
import ru.random.walk.chat_service.model.dto.response.message.PayloadDto;

@Getter
public class TextDto extends PayloadDto {
    private final String text;

    @SuppressWarnings("unused")
    public TextDto(String text) {
        super("text");
        this.text = text;
    }

    @SuppressWarnings("unused")
    public TextDto(String text, String type) {
        super(type);
        this.text = text;
    }
}
