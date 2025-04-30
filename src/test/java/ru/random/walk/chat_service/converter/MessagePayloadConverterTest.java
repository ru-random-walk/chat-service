package ru.random.walk.chat_service.converter;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessagePayloadConverterTest {
    private static final MessagePayloadConverter converter = new MessagePayloadConverter();

    @Test
    public void testSerialization() throws JSONException {
        RequestForWalkPayload requestForWalkPayload = new RequestForWalkPayload(
                new LocationPayload(9.4, 2.3, "Moscow", "Park culture", null),
                LocalDateTime.of(2024, 9, 22, 18, 0),
                false,
                UUID.fromString("f5bb8fcd-6117-41dc-9c9e-e45555c6c9d2")
        );
        String json = converter.convertToDatabaseColumn(requestForWalkPayload);
        JSONAssert.assertEquals("""
                {
                    "type":"request_for_walk",
                    "location": {
                        "type":"location",
                        "longitude":9.4,
                        "latitude":2.3,
                        "city":"Moscow",
                        "street":"Park culture",
                        "building":null
                    },
                    "startsAt":"18:00 22-09-2024",
                    "answer":false,
                    "appointmentId":"f5bb8fcd-6117-41dc-9c9e-e45555c6c9d2"
                }""", json, JSONCompareMode.STRICT);
    }

    @Test
    public void testSerializationWithEmptyFields() throws JSONException {
        RequestForWalkPayload requestForWalkPayload = new RequestForWalkPayload(
                new LocationPayload(9.4, 2.3, "Moscow", "Park culture", null),
                LocalDateTime.of(2024, 9, 22, 18, 0)
        );
        String json = converter.convertToDatabaseColumn(requestForWalkPayload);
        JSONAssert.assertEquals("""
                {
                    "type":"request_for_walk",
                    "location": {
                        "type":"location",
                        "longitude":9.4,
                        "latitude":2.3,
                        "city":"Moscow",
                        "street":"Park culture",
                        "building":null
                    },
                    "startsAt":"18:00 22-09-2024"
                }""", json, JSONCompareMode.STRICT);
    }

    @Test
    public void testDeserialization() {
        String json = """
                {
                    "type":"request_for_walk",
                    "location": {
                        "type":"location",
                        "longitude":9.4,
                        "latitude":2.3,
                        "city":"Moscow",
                        "street":"Park culture",
                        "building":null
                    },
                    "startsAt":"18:00 22-09-2024",
                    "answer":true,
                    "appointmentId":"f5bb8fcd-6117-41dc-9c9e-e45555c6c9d2"
                }""";
        RequestForWalkPayload requestForWalkPayload = (RequestForWalkPayload) converter.convertToEntityAttribute(json);
        assertEquals(
                LocalDateTime.of(2024, 9, 22, 18, 0),
                requestForWalkPayload.getStartsAt()
        );
    }

    @Test
    public void testDeserializationWithNullableFields() {
        String json = """
                {
                    "type":"request_for_walk",
                    "location": {
                        "type":"location",
                        "longitude":9.4,
                        "latitude":2.3,
                        "city":"Moscow",
                        "street":"Park culture",
                        "building":null
                    },
                    "startsAt":"18:00 22-09-2024",
                    "answer":null,
                    "appointmentId":null
                }""";
        RequestForWalkPayload requestForWalkPayload = (RequestForWalkPayload) converter.convertToEntityAttribute(json);
        assertEquals(
                LocalDateTime.of(2024, 9, 22, 18, 0),
                requestForWalkPayload.getStartsAt()
        );
    }

    @Test
    public void testDeserializationWithEmptyFields() {
        String json = """
                {
                    "type":"request_for_walk",
                    "location": {
                        "type":"location",
                        "longitude":9.4,
                        "latitude":2.3,
                        "city":"Moscow",
                        "street":"Park culture",
                        "building":null
                    },
                    "startsAt":"18:00 22-09-2024"
                }""";
        RequestForWalkPayload requestForWalkPayload = (RequestForWalkPayload) converter.convertToEntityAttribute(json);
        assertEquals(
                LocalDateTime.of(2024, 9, 22, 18, 0),
                requestForWalkPayload.getStartsAt()
        );
    }
}
