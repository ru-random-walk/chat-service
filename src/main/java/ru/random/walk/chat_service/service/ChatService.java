package ru.random.walk.chat_service.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.dto.CreatePrivateChatEvent;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    List<ChatDto> getChatPageByMemberUsername(Pageable pageable, UUID memberUsername);

    @Transactional
    void create(CreatePrivateChatEvent event);
}
