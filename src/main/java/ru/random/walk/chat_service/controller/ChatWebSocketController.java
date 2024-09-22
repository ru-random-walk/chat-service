package ru.random.walk.chat_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.random.walk.chat_service.dto.response.Message;

import java.security.Principal;
import java.util.UUID;

@Controller
@Slf4j
public class ChatWebSocketController {
    @MessageMapping("/{chatId}")
    public void sendMessage(Principal principal, @DestinationVariable UUID chatId, @RequestBody Message message) {
        log.info("{} sending message {} to chat {}", principal.getName(), message.id(), chatId);
    }
}
