package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.mapper.ChatMapper;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.repository.ChatMemberRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {
    private final ChatMemberRepository chatMemberRepository;
    private final ChatMapper chatMapper;

    public Page<ChatDto> getChatPageByMemberUsername(Pageable pageable, UUID memberUsername) {
        return chatMemberRepository.findAllById_UserId(memberUsername, pageable)
                .map(chatMapper::chatMemberToChatDto);
    }
}
