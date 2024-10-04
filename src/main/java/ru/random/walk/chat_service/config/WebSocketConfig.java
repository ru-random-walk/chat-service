package ru.random.walk.chat_service.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.random.walk.chat_service.config.properties.BrokerProps;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final BrokerProps brokerProps;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost(brokerProps.host())
                .setRelayPort(brokerProps.port())
                .setClientLogin(brokerProps.clientLogin())
                .setClientPasscode(brokerProps.clientPasscode())
                .setSystemLogin(brokerProps.systemLogin())
                .setSystemPasscode(brokerProps.systemPasscode())
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
