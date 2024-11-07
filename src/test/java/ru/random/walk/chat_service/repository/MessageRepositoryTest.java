package ru.random.walk.chat_service.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.TestContainersEnvironment;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.payload.TextPayload;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.model.entity.type.MessageType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MessageRepositoryTest extends TestContainersEnvironment {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional(readOnly = true)
    void save() {
        var chat = chatRepository.save(
                ChatEntity.builder()
                        .type(ChatType.PRIVATE)
                        .build()
        );
        var message = messageRepository.save(
                MessageEntity.builder()
                        .chatId(chat.getId())
                        .sentAt(LocalDateTime.now())
                        .payload(new TextPayload("Hi!"))
                        .type(MessageType.TEXT)
                        .build()
        );
        var savedMessage = messageRepository.findById(message.getId());
        assertTrue(savedMessage.isPresent());
        var entity = savedMessage.get();
        var textPayload = assertInstanceOf(TextPayload.class, entity.getPayload());
        assertEquals(textPayload.getText(), "Hi!");
    }
}