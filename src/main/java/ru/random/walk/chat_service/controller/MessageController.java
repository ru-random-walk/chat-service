package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.controller.validation.PageableConstraint;
import ru.random.walk.chat_service.model.dto.response.MessageDto;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("unused")
@Slf4j
@Tag(name = "REST Message Controller")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Operation(summary = "Message List")
    @GetMapping("/list")
    public Page<MessageDto> getHistory(
            Principal principal,
            @PageableConstraint(maxPageSize = 100, message = "PageSize must be <= 100!")
            Pageable pageable,
            @RequestParam @Schema(example = "919add90-0614-4ba4-b808-e423abfab4b6") UUID chatId,
            @RequestParam(required = false) String message,
            @RequestParam(required = false)
            @Schema(example = "18:00 22-09-2024")
            @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
            LocalDateTime from,
            @RequestParam(required = false)
            @Schema(example = "18:00 22-09-2024")
            @DateTimeFormat(pattern = "HH:mm dd-MM-yyyy")
            LocalDateTime to
    ) {
        log.info("""
                        Get message history for [{}]
                        with login [{}]
                        with chat id [{}]
                        with pageable [{}]
                        with message filter [{}]
                        with from filter [{}]
                        with to filter [{}]
                        """,
                principal, principal.getName(), chatId, pageable, message, from, to);
        return null;
    }
}
