package ru.random.walk.chat_service.service.auth.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
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
    public Principal getPrincipal(SimpMessageHeaderAccessor headerAccessor) {
        var token = headerAccessor.getFirstNativeHeader("Authorization");
        return fromBearerToken(token);
    }

    @Override
    public void authSubscriber(SessionSubscribeEvent subscriberEvent) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.getAccessor(
                subscriberEvent.getMessage(),
                SimpMessageHeaderAccessor.class
        );
        if (headerAccessor == null) {
            throw new AuthenticationException("Illegal authorization!");
        }
        var principal = getPrincipal(headerAccessor);
        var chatId = getChatId(headerAccessor);
        authByChatId(principal, chatId);
    }

    private static UUID getChatId(SimpMessageHeaderAccessor headerAccessor) {
        var rawDestination = headerAccessor.getHeader("simpDestination");
        if (!(rawDestination instanceof String destination) || !destination.startsWith("/topic/chat/")) {
            throw new AuthenticationException("Illegal authorization!");
        }
        return UUID.fromString(destination.substring(12));
    }

    private Principal fromBearerToken(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new AuthenticationException("Illegal authorization!");
        }
        String token = bearerToken.substring(7);
        Jwt jwt = jwtDecoder.decode(token);
        return jwtAuthenticationConverter.convert(jwt);
    }
}