package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.mapper.ChatMapper;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.model.entity.ChatEntity;
import ru.random.walk.chat_service.model.entity.ChatMemberEntity;
import ru.random.walk.chat_service.model.entity.type.ChatType;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.repository.ChatRepository;
import ru.random.walk.chat_service.service.ChatService;
import ru.random.walk.dto.CreatePrivateChatEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMemberRepository chatMemberRepository;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public List<ChatDto> getChatPageByMemberUsername(Pageable pageable, UUID memberUsername) {
        var chatWithMemberEntityList = chatMemberRepository.findAllChatWithMembersByUserId(memberUsername, pageable);
        return chatMapper.toDto(chatWithMemberEntityList);
    }

    @Transactional
    @Override
    public void create(CreatePrivateChatEvent event) {
        var chatIds = chatMemberRepository.findAllChatIdByUserIds(Set.of(
                event.chatMember1(),
                event.chatMember2()
        ));
        if (!chatIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Chat with members: [%s, %s] already exist!".formatted(event.chatMember1(), event.chatMember2())
            );
        }
        var chat = ChatEntity.builder().type(ChatType.PRIVATE).build();
        var savedChat = chatRepository.save(chat);
        var chatMember1 = ChatMemberEntity.builder()
                .chatId(savedChat.getId())
                .userId(event.chatMember1())
                .build();
        var chatMember2 = ChatMemberEntity.builder()
                .chatId(savedChat.getId())
                .userId(event.chatMember2())
                .build();
        chatMemberRepository.save(chatMember1);
        chatMemberRepository.save(chatMember2);
    }
}
