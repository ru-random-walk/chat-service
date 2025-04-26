package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.exception.AuthenticationException;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.service.Authenticator;

import java.security.Principal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticatorImpl implements Authenticator {
    private final ChatMemberRepository chatMemberRepository;

    @Override
    public void auth(Principal principal, UUID userId) {
        var login = UUID.fromString(principal.getName());
        if (!login.equals(userId)) {
            throw new AuthenticationException("MemberUsername mismatch with credentials!");
        }
    }

    @Override
    public void authByChatId(Principal principal, UUID chatId) {
        var login = UUID.fromString(principal.getName());
        chatMemberRepository.findAllByChatIdAndUserId(chatId, login)
                .orElseThrow(() -> new AuthenticationException("Chat with id: '%s' not found".formatted(chatId)));
    }

    @Override
    public void authSender(Principal principal, UUID sender, UUID chatId) {
        auth(principal, sender);
        authByChatId(principal, chatId);
    }
}