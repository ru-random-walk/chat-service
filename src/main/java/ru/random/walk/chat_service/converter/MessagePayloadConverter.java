package ru.random.walk.chat_service.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import ru.random.walk.chat_service.model.entity.payload.MessagePayload;

@Converter
@Slf4j
public class MessagePayloadConverter implements AttributeConverter<MessagePayload, String> {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String convertToDatabaseColumn(MessagePayload messagePayload) {
        try {
            var res = objectMapper.writeValueAsString(messagePayload);
            log.debug("Message payload: {}", res);
            return res;
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert messagePayload: [{}] to json", messagePayload, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessagePayload convertToEntityAttribute(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, MessagePayload.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert json: [{}] to entity payload", jsonStr, e);
            throw new RuntimeException(e);
        }
    }
}
