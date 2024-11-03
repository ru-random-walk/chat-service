package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.domain.PageRequest;
import ru.random.walk.chat_service.model.dto.response.Chat;
import ru.random.walk.chat_service.model.dto.response.Page;
import ru.random.walk.chat_service.repository.ChatMemberRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMemberRepository chatMemberRepository;

    public Page<Chat> getChatPageByMemberUsername(PageRequest pageRequest, UUID memberUsername) {
        var chatList = chatMemberRepository
                .findPageByUserId(memberUsername, pageRequest).stream()
                .map(chatMember -> new Chat(chatMember.getId().getChatId()))
                .toList();
        var total = chatMemberRepository.findTotalCountByUserId(memberUsername);
        return new Page<>(chatList, pageRequest.number(), pageRequest.size(), total);
    }
}
