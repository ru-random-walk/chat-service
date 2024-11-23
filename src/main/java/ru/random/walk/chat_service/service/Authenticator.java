package ru.random.walk.chat_service.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.exception.AuthenticationException;
import ru.random.walk.chat_service.repository.ChatMemberRepository;

import java.security.Principal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class Authenticator {
    private final ChatMemberRepository chatMemberRepository;

    public void auth(Principal principal, UUID userId) {
        var login = UUID.fromString(principal.getName());
        if (!login.equals(userId)) {
            throw new AuthenticationException("MemberUsername mismatch with credentials!");
        }
    }

    public void authByChatId(Principal principal, UUID chatId) {
        var member = chatMemberRepository.findById_ChatId(chatId)
                .orElseThrow(AuthenticationException::new);
        auth(principal, member.getId().getUserId());
    }
}
