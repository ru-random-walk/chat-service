package ru.random.walk.chat_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.random.walk.chat_service.mapper.MessageMapper;
import ru.random.walk.chat_service.model.dto.request.MessageRequestDto;
import ru.random.walk.chat_service.service.MessageService;
import ru.random.walk.chat_service.service.auth.Authenticator;

@SuppressWarnings("unused")
@Controller
@Slf4j
@AllArgsConstructor
public class ChatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final Authenticator authenticator;
    private final MessageMapper messageMapper;
    private final MessageService messageService;

    @MessageMapping("/sendMessage")
    public void sendMessage(SimpMessageHeaderAccessor headerAccessor, @RequestBody MessageRequestDto messageRequestDto) {
        var principal = authenticator.getPrincipal(headerAccessor);
        log.info("""
                        [{}] send message to chat
                        for [{}]
                        with header [{}]
                        """,
                principal.getName(), principal, messageRequestDto.chatId()
        );
        authenticator.authSender(principal, messageRequestDto.sender(), messageRequestDto.chatId());
        var message = messageMapper.toEntity(messageRequestDto);
        messageService.sendMessage(message);
    }
}