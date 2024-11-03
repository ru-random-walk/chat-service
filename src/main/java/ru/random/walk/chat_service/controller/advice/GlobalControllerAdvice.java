package ru.random.walk.chat_service.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.random.walk.chat_service.model.dto.ApiErrorDto;
import ru.random.walk.chat_service.model.exception.AuthenticationException;

import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorDto> unauthorizedException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiErrorDto.of(e.getMessage()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorDto> validationException(HandlerMethodValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorDto.of(
                        Arrays.stream(Objects.requireNonNull(e.getDetailMessageArguments()))
                                .map(Object::toString)
                                .reduce(String::concat)
                                .orElse("...")
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> unexpectedException(Exception e) {
        log.error("Unexpected exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiErrorDto.unexpectedError(e));
    }
}
