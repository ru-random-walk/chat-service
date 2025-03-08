package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.random.walk.chat_service.AbstractPostgresContainerTest;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.repository.ChatRepository;

import java.util.UUID;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ChatServiceTest extends AbstractPostgresContainerTest {
    private final ChatService chatService;
    @Autowired
    private ChatMemberRepository chatMemberRepository;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    void getChatPageByMemberUsername() {
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var member = chatMemberRepository.save(ChatMemberEntity.builder()
                .chatId(chat.getId())
                .userId(UUID.randomUUID())
                .build());
        chatService.getChatPageByMemberUsername(PageRequest.of(0, 10), member.getUserId());
    }
}