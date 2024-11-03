package ru.random.walk.chat_service.model.dto;

public record ApiErrorDto(
        String message
) {
    public static ApiErrorDto of(String message) {
        return new ApiErrorDto(message);
    }

    public static ApiErrorDto unexpectedError(Exception e) {
        return new ApiErrorDto("Unexpected error: " + e.getClass().getSimpleName());
    }
}
