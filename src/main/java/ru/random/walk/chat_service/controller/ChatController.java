package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.dto.response.Chat;
import ru.random.walk.chat_service.dto.response.Page;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
@RestController
@Slf4j
@RequestMapping("/chat")
@Tag(name = "REST Chat Controller")
public class ChatController {
    @Operation(summary = "Chat List")
    @GetMapping("/list")
    public Page<Chat> getChats(
            Principal principal,
            @RequestParam @Schema(example = "0") long pageNumber,
            @RequestParam @Schema(example = "1") long pageSize,
            @RequestParam(required = false) String memberUsername
    ) {
        log.info("""
                        Get chat list for [{}]
                        with login [{}]
                        with page number [{}]
                        with page size [{}]
                        with member username filter [{}]
                        """,
                principal, principal.getName(), pageNumber, pageSize, memberUsername);
        return new Page<>(List.of(new Chat(UUID.randomUUID())), 0, 0, 0);
    }
}
