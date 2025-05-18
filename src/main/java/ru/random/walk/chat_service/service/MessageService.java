package ru.random.walk.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.model.entity.MessageEntity;

import java.util.UUID;

public interface MessageService {
    Page<MessageDto> getMessagePageByChatIdAndFilter(Pageable pageable, UUID chatId, MessageFilter filter);

    void sendMessage(MessageEntity message);
}
