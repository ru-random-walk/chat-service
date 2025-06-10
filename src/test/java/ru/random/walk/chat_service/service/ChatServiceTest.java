package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.random.walk.chat_service.AbstractContainerTest;
import ru.random.walk.chat_service.model.domain.payload.TextPayload;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.dto.CreatePrivateChatEvent;

import java.time.LocalDateTime;
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

    private final MessageService messageService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    void getChatPageByMemberUsername() {
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat2 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat3 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        var userId = UUID.randomUUID();
        for (var chatId : List.of(chat.getId(), chat2.getId(), chat3.getId())) {
            chatMemberRepository.save(ChatMemberEntity.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build());
        }

        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat3.getId())
                .payload(new TextPayload("Third message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());
        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat2.getId())
                .payload(new TextPayload("Latest message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());

        // Пустой чат выводим вперед - для мотивирования начать разговор
        var chatList = chatService.getChatPageByMemberUsername(PageRequest.of(0, 10), userId);
        assertEquals(3, chatList.size());
        assertEquals(
                chatList.stream()
                        .map(ChatDto::id)
                        .toList(),
                List.of(
                        chat.getId(),
                        chat2.getId(),
                        chat3.getId()
                )
        );
    }

    @Test
    void getChatPageByMemberUsernameMore() {
        var chat = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat2 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat3 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat4 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat5 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());
        var chat6 = chatRepository.save(ChatEntity.builder()
                .type(ChatType.PRIVATE)
                .build());

        var userId = UUID.randomUUID();
        for (var chatId : List.of(
                chat.getId(), chat2.getId(), chat3.getId(), chat4.getId(), chat5.getId(), chat6.getId()
        )) {
            chatMemberRepository.save(ChatMemberEntity.builder()
                    .chatId(chatId)
                    .userId(userId)
                    .build());
        }

        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat6.getId())
                .payload(new TextPayload("Third message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());
        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat5.getId())
                .payload(new TextPayload("Third message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());
        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat4.getId())
                .payload(new TextPayload("Latest message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());
        messageService.sendMessage(MessageEntity.builder()
                .chatId(chat3.getId())
                .payload(new TextPayload("Third message"))
                .sender(userId)
                .recipient(userId)
                .sentAt(LocalDateTime.now())
                .build());

        // Пустой чат выводим вперед - для мотивирования начать разговор
        var chatList = chatService.getChatPageByMemberUsername(PageRequest.of(0, 10), userId);
        assertEquals(3, chatList.size());
        assertEquals(
                chatList.stream()
                        .map(ChatDto::id)
                        .toList(),
                List.of(
                        chat.getId(),
                        chat2.getId(),
                        chat3.getId(),
                        chat4.getId(),
                        chat5.getId(),
                        chat6.getId()
                )
        );
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