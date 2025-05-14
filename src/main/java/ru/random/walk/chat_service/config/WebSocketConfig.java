package ru.random.walk.chat_service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.random.walk.chat_service.config.properties.BrokerProps;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final BrokerProps brokerProps;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

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
                .setClientLogin(brokerProps.login())
                .setClientPasscode(brokerProps.password())
                .setSystemLogin(brokerProps.login())
                .setSystemPasscode(brokerProps.password())
                .setUserDestinationBroadcast("/topic/unresolved-user")
                .setUserRegistryBroadcast("/topic/log-user-registry");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                log.info("PreSendMethod message: [{}]", message);
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7); // Extract the token
                        try {
                            // Validate the token and extract user information
                            // This is a placeholder; use a JWT library to decode and validate the token
                            String user = validateTokenAndGetUser(token);
                            accessor.setUser(() -> user); // Set the user
                        } catch (Exception e) {
                            log.error("Invalid token", e);
                            // Handle invalid token
                        }
                    }
                }
                return message;
            }

            private String validateTokenAndGetUser(String token) {
                // Create a Jwt object from the token string
                Jwt jwt = Jwt.withTokenValue(token).build();

                // Convert the Jwt to an Authentication object
                // This step may involve validating the token signature, issuer, audience, etc.
                // You might need to configure a JwtDecoder with the necessary validation logic
                Authentication authentication = jwtAuthenticationConverter.convert(jwt);

                // Extract the user information from the Authentication object
                // This could be the username, user ID, or any other user-related information
                if (authentication.isAuthenticated()) {
                    return authentication.getName(); // or any other user information you need
                } else {
                    throw new RuntimeException("Invalid token");
                }
            }
        });
    }
}
