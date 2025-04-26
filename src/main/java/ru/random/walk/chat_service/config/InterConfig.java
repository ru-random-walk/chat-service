package ru.random.walk.chat_service.config;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "inter", ignoreUnknownFields = false)
@Builder
public record InterConfig(
        MatherConfig matcher,
        AuthConfig auth
) {
    @Builder
    public record MatherConfig(
            String url
    ) {
    }

    @Builder
    public record AuthConfig(
            String url,
            String login,
            String password
    ) {
    }
}
