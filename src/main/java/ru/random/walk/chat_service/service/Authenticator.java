package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.exception.AuthenticationException;
import ru.random.walk.chat_service.repository.ChatMemberRepository;

import java.security.Principal;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class Authenticator {
    private final ChatMemberRepository chatMemberRepository;

    public void auth(Principal principal, UUID userId) {
        if (authPredicate(principal).negate().test(userId)) {
            throw new AuthenticationException("MemberUsername mismatch with credentials!");
        }
    }

    public void authByChatId(Principal principal, UUID chatId) {
        var login = UUID.fromString(principal.getName());
        chatMemberRepository.findById_UserId(login).stream()
                .map(chatMemberEntity -> chatMemberEntity.getId().getChatId())
                .filter(id->id.equals(chatId))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException("Chat with id: '%s' not found".formatted(chatId)));
    }

    private Predicate<UUID> authPredicate(Principal principal) {
        return id -> {
            var login = UUID.fromString(principal.getName());
            return login.equals(id);
        };
    }

    public void authSender(Principal principal, UUID sender, UUID chatId) {
        auth(principal, sender);
        authByChatId(principal, chatId);
    }
}