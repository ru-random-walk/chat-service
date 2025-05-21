package ru.random.walk.chat_service.controller.websocket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.random.walk.chat_service.service.auth.Authenticator;

@Component
@Slf4j
@AllArgsConstructor
public class WebsocketEventListener {
    private final Authenticator authenticator;

    @EventListener
    private void handleSubscribe(SessionSubscribeEvent subscribeEvent) {
        log.info("Handle subscribe event: {}", subscribeEvent);
        authenticator.authSubscriber(subscribeEvent);
    }
}
