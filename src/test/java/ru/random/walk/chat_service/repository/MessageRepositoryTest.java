package ru.random.walk.chat_service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import ru.random.walk.chat_service.AbstractPostgresContainerTest;
import ru.random.walk.chat_service.model.domain.payload.LocationPayload;
import ru.random.walk.chat_service.model.domain.payload.MessagePayload;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.domain.payload.TextPayload;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.model.entity.type.MessageType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MessageRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    void save() {
        var chat = chatRepository.save(
                ChatEntity.builder()
                        .type(ChatType.PRIVATE)
                        .build()
        );
        var createdMessage = messageRepository.save(
                MessageEntity.builder()
                        .chatId(chat.getId())
                        .payload(new TextPayload("Hi!"))
                        .build()
        );
        assertNotNull(createdMessage.getSentAt());
        var foundMessage = messageRepository.findById(createdMessage.getId());
        assertTrue(foundMessage.isPresent());
        var entity = foundMessage.get();
        var textPayload = assertInstanceOf(TextPayload.class, entity.getPayload());
        assertEquals(textPayload.getText(), "Hi!");
    }

    @Test
    void createMessagesWithDifferentTypes() {
        var chat = chatRepository.save(
                ChatEntity.builder()
                        .type(ChatType.PRIVATE)
                        .build()
        );
        record PayloadWithType(MessagePayload messagePayload, MessageType messageType) {
        }
        List<PayloadWithType> payloads = List.of(
                new PayloadWithType(new TextPayload("Hi!!!!"), MessageType.TEXT),
                new PayloadWithType(
                        new RequestForWalkPayload(
                                new LocationPayload(23234.12, 14134.24),
                                LocalDateTime.now()
                        ),
                        MessageType.REQUEST_FOR_WALK
                )
        );
        List<UUID> createdMessageIds = new ArrayList<>();
        for (PayloadWithType payload : payloads) {
            var createdMessage = messageRepository.save(
                    MessageEntity.builder()
                            .chatId(chat.getId())
                            .payload(payload.messagePayload)
                            .build()
            );
            assertNotNull(createdMessage.getSentAt());
            createdMessageIds.add(createdMessage.getId());
        }
        for (UUID createdMessageId : createdMessageIds) {
            var foundMessage = messageRepository.findById(createdMessageId);
            assertTrue(foundMessage.isPresent());
            var message = foundMessage.get();
            assertNotNull(message);
        }

        var page = messageRepository.findByChatId(chat.getId(), null, null, null, Pageable.ofSize(100));
        assertEquals(payloads.size(), page.getTotalElements());
    }
}