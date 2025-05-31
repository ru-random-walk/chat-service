package ru.random.walk.chat_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.mapper.MessageMapper;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class MessageControllerTest extends AbstractContainerTest {
    private ChatRepository chatRepository;
    private MessageRepository messageRepository;
    private MessageMapper messageMapper;
    private ObjectMapper objectMapper;

    @Test
    void testMessageDateSavingWithCorrectView() throws JSONException {
        var chat = chatRepository.save(
                ChatEntity.builder()
                        .type(ChatType.PRIVATE)
                        .build()
        );
        var message = messageRepository.saveAndFlush(MessageEntity.builder()
                .chatId(chat.getId())
                .sender(UUID.randomUUID())
                .recipient(UUID.randomUUID())
                .markedAsRead(true)
                .build());
        message.setSentAt(LocalDateTime.of(2025, 4, 4, 22, 0));
        messageRepository.saveAndFlush(message);
        var messageDto = messageMapper.toDto(message);
        var messageView = assertDoesNotThrow(() -> objectMapper.writeValueAsString(messageDto));
        JSONAssert.assertEquals("""
                        {
                            "id":"%s",
                            "payload":null,
                            "chatId":"%s",
                            "markedAsRead":true,
                            "createdAt":"22:00 04-04-2025",
                            "sender":"%s"
                        }
                        """.formatted(message.getId(), chat.getId(), message.getSender()),
                messageView,
                JSONCompareMode.NON_EXTENSIBLE
        );
    }
}