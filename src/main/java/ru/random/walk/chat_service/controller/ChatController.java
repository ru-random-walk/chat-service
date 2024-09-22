package ru.random.walk.chat_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.random.walk.chat_service.dto.chat.Chat;
import ru.random.walk.chat_service.dto.chat.ChatPageRequest;
import ru.random.walk.chat_service.dto.message.Message;
import ru.random.walk.chat_service.dto.message.MessagePageRequest;
import ru.random.walk.chat_service.dto.pagination.Page;

import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {
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
