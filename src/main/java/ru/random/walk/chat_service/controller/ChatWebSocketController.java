package ru.random.walk.chat_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.random.walk.chat_service.dto.request.MessageRequest;

import java.security.Principal;

@Controller
@Slf4j
public class ChatWebSocketController {
    @MessageMapping
    public void sendMessage(Principal principal, @RequestBody MessageRequest messageRequest) {
        log.info("{} sending message to chat {}", principal.getName(), messageRequest.chatId());
    }
}
