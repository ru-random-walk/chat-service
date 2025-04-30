package ru.random.walk.chat_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.chat_service.service.ChatService;
import ru.random.walk.dto.CreatePrivateChatEvent;
import ru.random.walk.dto.RequestedAppointmentStateEvent;

@RestController
@RequestMapping("/test")
@Tag(name = "REST Integration Test Controller")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize(value = "hasRole('TESTER')")
public class IntegrationTestController {
    private final ChatService chatService;
    private final AppointmentService appointmentService;

    @Operation(summary = "Create private chat event - Kafka integration test")
    @PostMapping("/create-private-chat-event")
    public void createChat(@RequestBody CreatePrivateChatEvent event) {
        log.info("Received test event: {}", event);
        chatService.create(event);
    }

    @Operation(summary = "Send requested appointment state event - Kafka integration test")
    @PostMapping("/send-requested-appointment-state-event")
    public void sendRequestedAppointmentStateEvent(RequestedAppointmentStateEvent event) {
        log.info("Received requested appointment state event: {}", event);
        appointmentService.updateState(event);
    }
}
