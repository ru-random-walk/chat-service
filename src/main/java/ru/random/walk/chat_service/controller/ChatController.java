package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.model.domain.PageRequest;
import ru.random.walk.chat_service.model.dto.response.Chat;
import ru.random.walk.chat_service.model.dto.response.Page;
import ru.random.walk.chat_service.service.Authenticator;
import ru.random.walk.chat_service.service.ChatService;

import java.security.Principal;
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
    public Page<Chat> getChats(
            Principal principal,
            @RequestParam
            @Schema(example = "0")
            @Min(value = 0, message = "pageNumber must be >= 0")
            Integer pageNumber,
            @RequestParam
            @Schema(example = "15")
            @Range(min = 1, max = 15, message = "pageSize must be in range from 1 to 15")
            Integer pageSize,
            @RequestParam UUID memberUsername
    ) {
        log.info("""
                        Get chat list for [{}]
                        with login [{}]
                        with page number [{}]
                        with page size [{}]
                        with member username [{}]
                        """,
                principal, principal.getName(), pageNumber, pageSize, memberUsername);
        authenticator.auth(principal, memberUsername);
        return chatService.getChatPageByMemberUsername(PageRequest.of(pageNumber, pageSize), memberUsername);
    }
}
