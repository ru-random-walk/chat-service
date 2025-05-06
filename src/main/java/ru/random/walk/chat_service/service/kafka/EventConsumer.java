package ru.random.walk.chat_service.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.chat_service.service.ChatService;
import ru.random.walk.dto.CreatePrivateChatEvent;
import ru.random.walk.dto.RequestedAppointmentStateEvent;
import ru.random.walk.topic.EventTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final ChatService chatService;
    private final AppointmentService appointmentService;

    @KafkaListener(topics = EventTopic.CREATE_CHAT)
    public void listenCreatePrivateChatEvent(CreatePrivateChatEvent event) {
        log.info("Received create chat event: {}", event);
        chatService.create(event);
    }

    @KafkaListener(topics = EventTopic.REQUESTED_APPOINTMENT_STATE)
    public void listenRequestedAppointmentStateEvent(RequestedAppointmentStateEvent event) {
        log.info("Received requested appointment state event: {}", event);
        appointmentService.updateState(event);
    }
}