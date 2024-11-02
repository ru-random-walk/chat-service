package ru.random.walk.chat_service.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.random.walk.chat_service.model.dto.ApiErrorDto;
import ru.random.walk.chat_service.model.exception.AuthenticationException;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorDto> unauthorizedException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiErrorDto(e.getMessage()));
    }
}
