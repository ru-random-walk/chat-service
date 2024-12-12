package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.mapper.MessageMapper;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.repository.MessageRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    public Page<MessageDto> getMessagePageByChatIdAndFilter(Pageable pageable, UUID chatId, MessageFilter filter) {
        log.info("getMessagePageByChatIdAndFilter: {} {}", chatId, filter);
        return messageRepository.findByChatId(chatId, filter.message(), filter.from(), filter.to(), pageable)
                .map(messageMapper::toDto);
    }

    public void sendMessage(MessageEntity message) {
        messageRepository.save(message);
        if (isUserConnected(message.getSender())) {
            messagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), message);
        }
    }

    private boolean isUserConnected(UUID user) {
        var userId = user.toString();
        return Objects.nonNull(userRegistry.getUser(userId));
    }
}