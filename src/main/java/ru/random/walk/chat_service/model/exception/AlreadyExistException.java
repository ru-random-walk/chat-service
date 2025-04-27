package ru.random.walk.chat_service.model.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
    }
}
