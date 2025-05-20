package ru.random.walk.chat_service.service.auth;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.security.Principal;
import java.util.UUID;

public interface Authenticator {
    void auth(Principal principal, UUID userId);

    void authByChatId(Principal principal, UUID chatId);

    void authSender(Principal principal, UUID sender, UUID chatId);

    Principal getPrincipal(SimpMessageHeaderAccessor headerAccessor);
}
