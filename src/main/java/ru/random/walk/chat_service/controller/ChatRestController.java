package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.dto.chat.Chat;
import ru.random.walk.chat_service.dto.message.Message;
import ru.random.walk.chat_service.dto.pagination.Page;
import ru.random.walk.chat_service.dto.pagination.PageRequest;

import java.security.Principal;
import java.time.LocalDateTime;
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
    public Page<Message> getHistory(
            Principal principal,
            @PathVariable UUID chatId,
            @RequestBody PageRequest pageRequest,
            @RequestParam(required = false) Optional<String> message,
            @RequestParam(required = false)
            @Schema(example = "18:00 22-09-2024")
            @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
            Optional<LocalDateTime> from,
            @RequestParam(required = false)
            @Schema(example = "18:00 22-09-2024")
            @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
            Optional<LocalDateTime> to
    ) {
        log.info("""
                        Get message history for [{}]
                        with chat id [{}]
                        with page request [{}]
                        with message filter [{}]
                        with from filter [{}]
                        with to filter [{}]
                        """,
                principal, chatId, pageRequest, message, from, to);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }

    @Operation(summary = "Get chat list")
    @PostMapping("/list")
    public Page<Chat> getChats(
            Principal principal,
            @RequestBody PageRequest pageRequest,
            @RequestParam(required = false) Optional<String> memberUsername
    ) {
        log.info("""
                        Get chat list for [{}]
                        with page request [{}]
                        with member username filter [{}]
                        """,
                principal, pageRequest, memberUsername);
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }
}
