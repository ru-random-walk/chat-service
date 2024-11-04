package ru.random.walk.chat_service.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import ru.random.walk.chat_service.model.dto.request.MessageRequestDto;

import java.security.Principal;

@SuppressWarnings("unused")
@Controller
@Slf4j
@AllArgsConstructor
public class ChatWebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/sendMessage")
    public void sendMessage(Principal principal, @RequestBody MessageRequestDto messageRequestDto) {
        log.info("""
                        [{}] send message to chat
                        with login [{}]
                        with chat id [{}]
                        """,
                principal, principal.getName(), messageRequestDto.chatId());
        messagingTemplate.convertAndSend("/topic/chat/" + messageRequestDto.chatId(), messageRequestDto);
    }
}
