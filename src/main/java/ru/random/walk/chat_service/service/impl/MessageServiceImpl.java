package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.mapper.MessageMapper;
import ru.random.walk.chat_service.model.domain.MessageFilter;
import ru.random.walk.chat_service.model.domain.OutboxAdditionalInfoKey;
import ru.random.walk.chat_service.model.domain.OutboxHttpTopic;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;
import ru.random.walk.chat_service.model.dto.response.MessageDto;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.model.exception.ValidationException;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.service.MessageService;
import ru.random.walk.chat_service.service.NotificationSender;
import ru.random.walk.chat_service.service.OutboxSenderService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final OutboxSenderService outboxSenderService;
    private final NotificationSender notificationSender;

    @Override
    public Page<MessageDto> getMessagePageByChatIdAndFilter(Pageable pageable, UUID chatId, MessageFilter filter) {
        log.info("getMessagePageByChatIdAndFilter: {} {}", chatId, filter);
        return messageRepository.findByChatId(chatId, filter.message(), filter.from(), filter.to(), pageable)
                .map(messageMapper::toDto);
    }

    @Override
    @Transactional
    public void sendMessage(MessageEntity message) {
        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), message);
        if (message.getPayload() instanceof RequestForWalkPayload requestForWalkPayload) {
            OffsetDateTime startTime = requestForWalkPayload.getStartsAt()
                    .atZone(ZoneOffset.systemDefault()).toOffsetDateTime();
            checkStartTime(startTime);
            outboxSenderService.sendMessage(
                    OutboxHttpTopic.SEND_CREATING_APPOINTMENT_TO_MATCHER,
                    RequestForAppointmentDto.builder()
                            .requesterId(message.getSender())
                            .partnerId(message.getRecipient())
                            .startTime(startTime)
                            .longitude(requestForWalkPayload.getLocation().getLongitude())
                            .latitude(requestForWalkPayload.getLocation().getLatitude())
                            .build(),
                    Map.of(OutboxAdditionalInfoKey.MESSAGE_ID.name(), message.getId().toString())
            );
        }
        notificationSender.notifyAboutNewMessage(message);
    }

    private static void checkStartTime(OffsetDateTime startTime) {
        var now = OffsetDateTime.now();
        if (startTime.isBefore(now)) {
            throw new ValidationException();
        }
    }
}