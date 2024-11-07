package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.mapper.MessageMapper;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.repository.MessageRepository;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Page<MessageDto> getMessagePageByChatIdAndFilter(Pageable pageable, UUID chatId, MessageFilter filter) {
        log.info("getMessagePageByChatIdAndFilter: {} {}", chatId, filter);
        var messageFilter = getMessageFilter(filter);
        return messageRepository.findByChatId(chatId, messageFilter, filter.from(), filter.to(), pageable)
                .map(messageMapper::toDto);
    }

    private static String getMessageFilter(MessageFilter filter) {
        if (Objects.isNull(filter.message()) || filter.message().isEmpty()) {
            return "%";
        }
        return "%" + filter.message() + "%";
    }
}
