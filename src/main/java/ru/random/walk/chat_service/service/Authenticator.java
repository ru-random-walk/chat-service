package ru.random.walk.chat_service.service;

import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.model.exception.AuthenticationException;

import java.security.Principal;
import java.util.UUID;

@Service
public class Authenticator {
    public void auth(Principal principal, UUID userId) {
        var login = UUID.fromString(principal.getName());
        if (!login.equals(userId)) {
            throw new AuthenticationException("MemberUsername mismatch with credentials!");
        }
    }
}
