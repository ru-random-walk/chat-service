package ru.random.walk.chat_service.model.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
