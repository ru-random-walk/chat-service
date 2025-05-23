package ru.random.walk.chat_service.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "broker")
public record BrokerProps(
        String host,
        int port,
        String login,
        String password
) {
}
