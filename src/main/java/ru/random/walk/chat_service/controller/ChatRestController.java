package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.dto.chat.Chat;
import ru.random.walk.chat_service.dto.chat.ChatPageRequest;
import ru.random.walk.chat_service.dto.message.Message;
import ru.random.walk.chat_service.dto.message.MessagePageRequest;
import ru.random.walk.chat_service.dto.pagination.Page;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/chat")
@Tag(name = "REST Controller for chat")
public class ChatRestController {
    @Operation(summary = "Get chat message history")
    @PostMapping("/{chatId}")
    public Page<Message> getHistory(Principal principal, @PathVariable UUID chatId, @RequestBody MessagePageRequest messagePageRequest) {
        log.info("{} getting history page {} for chat {}", Optional.ofNullable(principal).map(Principal::getName).orElse(null), messagePageRequest, chatId);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }

    @Operation(summary = "Get chat list")
    @PostMapping("/list")
    public Page<Chat> getChats(Principal principal, @RequestBody ChatPageRequest chatPageRequest) {
        log.info("{} getting history page {} for chats", Optional.ofNullable(principal).map(Principal::getName).orElse(null), chatPageRequest);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }
}
