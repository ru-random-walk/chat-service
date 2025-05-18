package ru.random.walk.chat_service.service.auth.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.exception.AuthenticationException;
import ru.random.walk.chat_service.repository.ChatMemberRepository;
import ru.random.walk.chat_service.service.auth.Authenticator;

import java.security.Principal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticatorImpl implements Authenticator {
    private final ChatMemberRepository chatMemberRepository;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtDecoder jwtDecoder;

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

    @Override
    public void authSender(SimpMessageHeaderAccessor headerAccessor, UUID sender) {
        var bearerToken = headerAccessor.getFirstNativeHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new AuthenticationException("Illegal authorization!");
        }
        String token = bearerToken.substring(7);
        Jwt jwt = jwtDecoder.decode(token);
        Authentication authentication = jwtAuthenticationConverter.convert(jwt);
        var login = UUID.fromString(authentication.getName());
        if (!login.equals(sender)) {
            throw new AuthenticationException("Sender mismatch with credentials!");
        }
    }
}