package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.dto.CreatePrivateChatEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ChatServiceTest extends AbstractContainerTest {
    private final ChatService chatService;
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;

    @Test
    void getChatPageByMemberUsername() {
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var member = chatMemberRepository.save(ChatMemberEntity.builder()
                .chatId(chat.getId())
                .userId(UUID.randomUUID())
                .build());
        var chatList = chatService.getChatPageByMemberUsername(PageRequest.of(0, 10), member.getUserId());
        assertEquals(1, chatList.size());
        assertEquals(chatList.getFirst(), ChatDto.builder()
                .id(chat.getId())
                .memberIds(List.of(member.getUserId()))
                .build());
    }

    @Test
    void createPrivateChatTwiceFailed() {
        var privateChatInfo = new CreatePrivateChatEvent(UUID.randomUUID(), UUID.randomUUID());
        chatService.create(privateChatInfo);
        assertThrows(RuntimeException.class, () -> chatService.create(privateChatInfo));
        var chatIds = chatMemberRepository.findAllChatIdWithUserIdsAsMembersForEach(Set.of(
                privateChatInfo.chatMember1(),
                privateChatInfo.chatMember2()
        ));
        assertEquals(1, chatIds.size());
    }

    @Test
    void createPrivateChatsSuccess() {
        var user1 = UUID.randomUUID();
        var user2 = UUID.randomUUID();
        var user3 = UUID.randomUUID();
        var user4 = UUID.randomUUID();
        var privateChatInfo = new CreatePrivateChatEvent(user1, user2);
        var privateChatInfo2 = new CreatePrivateChatEvent(user3, user4);
        var privateChatInfo3 = new CreatePrivateChatEvent(user1, user3);
        chatService.create(privateChatInfo);
        chatService.create(privateChatInfo2);
        chatService.create(privateChatInfo3);
    }
}