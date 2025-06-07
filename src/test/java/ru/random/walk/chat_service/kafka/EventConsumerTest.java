package ru.random.walk.chat_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.entity.UserEntity;
import ru.random.walk.chat_service.repository.UserRepository;
import ru.random.walk.dto.RegisteredUserInfoEvent;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class EventConsumerTest extends AbstractContainerTest {
    private final EventConsumer eventConsumer;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Test
    void testListenUserRegistration() throws JsonProcessingException {
        var userId = UUID.randomUUID();
        var event = RegisteredUserInfoEvent.builder()
                .fullName("Ficus")
                .id(userId)
                .build();
        eventConsumer.listenUserRegistration(objectMapper.writeValueAsString(event));
        UserEntity user = userRepository.findById(userId).orElseThrow();
        assertEquals("Ficus", user.getFullName());
    }

    @Test
    void testListenUserRegistrationUpdateInfo() throws JsonProcessingException {
        var userId = UUID.randomUUID();
        var event = RegisteredUserInfoEvent.builder()
                .fullName("Ficus")
                .id(userId)
                .build();
        eventConsumer.listenUserRegistration(objectMapper.writeValueAsString(event));
        var updateEvent = RegisteredUserInfoEvent.builder()
                .fullName("Kukis")
                .id(userId)
                .build();
        eventConsumer.listenUserRegistration(objectMapper.writeValueAsString(updateEvent));
        UserEntity user = userRepository.findById(userId).orElseThrow();
        assertEquals("Kukis", user.getFullName());
    }
}