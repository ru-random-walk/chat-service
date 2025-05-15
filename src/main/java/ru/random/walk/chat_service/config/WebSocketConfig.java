package ru.random.walk.chat_service.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;
import ru.random.walk.chat_service.config.properties.BrokerProps;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final BrokerProps brokerProps;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtDecoder jwtDecoder;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(
                            @NonNull ServerHttpRequest request,
                            @NonNull ServerHttpResponse response,
                            @NonNull WebSocketHandler wsHandler,
                            @NonNull Map<String, Object> attributes
                    ) {
                        log.info("beforeHandshake with {} {} {} {}", request, response, wsHandler, attributes);
                        return true;
                    }

                    @Override
                    public void afterHandshake(
                            ServerHttpRequest request,
                            ServerHttpResponse response,
                            WebSocketHandler wsHandler,
                            Exception exception
                    ) {

                    }
                })
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
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                log.info("preSend {} {}", message, channel);
                var commandTypes = Set.of(
                        StompCommand.SEND,
                        StompCommand.SUBSCRIBE
                );
                if (accessor != null && commandTypes.contains(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("Authorization");

                    if (authorization != null && !authorization.isEmpty()) {
                        String bearerToken = authorization.getFirst();
                        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                            String token = bearerToken.substring(7);

                            try {
                                Jwt jwt = jwtDecoder.decode(token);
                                Authentication authentication = jwtAuthenticationConverter.convert(jwt);
                                accessor.setUser(authentication);

                                log.info("WebSocket authenticated user: {}", authentication.getName());
                            } catch (Exception e) {
                                log.error("Failed to authenticate WebSocket connection", e);
                            }
                        }
                    }
                }

                return message;
            }
        });
    }
}
