package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.dto.response.Message;
import ru.random.walk.chat_service.dto.response.Page;
import ru.random.walk.chat_service.dto.response.message.Type;
import ru.random.walk.chat_service.dto.response.message.payload.Text;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "REST Message Controller")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Operation(summary = "Message List")
    @GetMapping("/list")
    public Page<Message> getHistory(
            Principal principal,
            @RequestParam @Schema(example = "919add90-0614-4ba4-b808-e423abfab4b6") UUID chatId,
            @RequestParam @Schema(example = "0") long pageNumber,
            @RequestParam @Schema(example = "1") long pageSize,
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
                        with chat id [{}]
                        with page number [{}]
                        with page size [{}]
                        with message filter [{}]
                        with from filter [{}]
                        with to filter [{}]
                        """,
                principal, chatId, pageNumber, pageSize, message, from, to);
        return new Page<>(List.of(
                new Message(
                        UUID.randomUUID(),
                        new Text("Some text message"),
                        Type.TEXT,
                        chatId,
                        true,
                        LocalDateTime.of(2024, 9, 22, 18, 0)
                )
        ), 0, 0, 0);
    }
}
