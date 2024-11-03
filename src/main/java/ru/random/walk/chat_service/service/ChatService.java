package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.repository.ChatMemberRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMemberRepository chatMemberRepository;

    public Page<ChatDto> getChatPageByMemberUsername(Pageable pageable, UUID memberUsername) {
        var chatList = chatMemberRepository
                .findPageByUserId(memberUsername, pageable).stream()
                .map(chatMember -> new ChatDto(chatMember.getId().getChatId()))
                .toList();
        var total = chatMemberRepository.findTotalCountByUserId(memberUsername);
        return new PageImpl<>(chatList, pageable, total);
    }
}
