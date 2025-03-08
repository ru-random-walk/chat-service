package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.controller.validation.PageableConstraint;
import ru.random.walk.chat_service.model.dto.response.ChatDto;
import ru.random.walk.chat_service.service.Authenticator;
import ru.random.walk.chat_service.service.ChatService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@Slf4j
@RequestMapping("/chat")
@Tag(name = "REST Chat Controller")
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final Authenticator authenticator;

    @Operation(summary = "Chat List")
    @GetMapping("/list")
    public List<ChatDto> getChats(
            Principal principal,
            @PageableConstraint(maxPageSize = 30, message = "Page size must be <= 30!")
            Pageable pageable,
            @RequestParam UUID memberUsername
    ) {
        log.info("""
                        Get chat list for [{}]
                        with login [{}]
                        with pageable [{}]
                        with member username [{}]
                        """,
                principal, principal.getName(), pageable, memberUsername);
        authenticator.auth(principal, memberUsername);
        return chatService.getChatPageByMemberUsername(pageable, memberUsername).toList();
    }
}
