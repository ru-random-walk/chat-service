package ru.random.walk.chat_service.repository;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.random.walk.chat_service.AbstractPostgresContainerTest;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;

import java.util.UUID;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ChatMemberRepositoryTest extends AbstractPostgresContainerTest {
    private ChatMemberRepository chatMemberRepository;
    private ChatRepository chatRepository;

    @Test
    void save() {
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        chatMemberRepository.save(ChatMemberEntity.builder()
                .chatId(chat.getId())
                .userId(UUID.randomUUID())
                .build());
    }
}