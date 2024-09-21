package ru.random.walk.chat_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.random.walk.chat_service.model.chat.Chat;
import ru.random.walk.chat_service.model.chat.ChatPageRequest;
import ru.random.walk.chat_service.model.message.Message;
import ru.random.walk.chat_service.model.message.MessagePageRequest;
import ru.random.walk.chat_service.model.pagination.Page;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    @MessageMapping("/{chatId}")
    public void sendMessage(Principal principal, @DestinationVariable UUID chatId, @RequestBody Message message) {
        log.info("{} sending message to chat {}", principal.getName(), chatId);
    }

    @GetMapping("/{chatId}")
    public Page<Message> getHistory(Principal principal, @PathVariable UUID chatId, @RequestBody MessagePageRequest messagePageRequest) {
        log.info("{} getting history page {} for chat {}", principal.getName(), messagePageRequest, chatId);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }

    @GetMapping("/list")
    public Page<Chat> getChats(Principal principal, @RequestBody ChatPageRequest chatPageRequest) {
        log.info("{} getting history page {} for chats", principal.getName(), chatPageRequest);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }
}
