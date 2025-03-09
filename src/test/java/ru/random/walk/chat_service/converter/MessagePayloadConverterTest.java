package ru.random.walk.chat_service.converter;

import org.junit.jupiter.api.Test;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagePayloadConverterTest {
    private static final MessagePayloadConverter converter = new MessagePayloadConverter();

    @Test
    public void testSerialization() {
        RequestForWalkPayload requestForWalkPayload = new RequestForWalkPayload(
                new LocationPayload(9.4, 2.3, "Moscow", "Park culture", null),
                LocalDateTime.of(2024, 9, 22, 18, 0)
        );
        String json = converter.convertToDatabaseColumn(requestForWalkPayload);
        assertEquals("""
                {"type":"request_for_walk","location":{"type":"location","longitude":9.4,"latitude":2.3,"city":"Moscow","street":"Park culture","building":null},"startsAt":"18:00 22-09-2024","answer":null}""", json);
    }

    @Test
    public void testDeserialization() {
        String json = """
                {"type":"request_for_walk","location":{"type":"location","longitude":9.4,"latitude":2.3,"city":"Moscow","street":"Park culture","building":null},"startsAt":"18:00 22-09-2024","answer":null}
                """;
        RequestForWalkPayload requestForWalkPayload = (RequestForWalkPayload) converter.convertToEntityAttribute(json);
        assertEquals(
                LocalDateTime.of(2024, 9, 22, 18, 0),
                requestForWalkPayload.getStartsAt()
        );
    }
}
