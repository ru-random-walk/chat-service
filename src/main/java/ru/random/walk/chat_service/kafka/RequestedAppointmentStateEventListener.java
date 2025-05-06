package ru.random.walk.chat_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.dto.RequestedAppointmentStateEvent;
import ru.random.walk.topic.EventTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestedAppointmentStateEventListener {
    private final AppointmentService appointmentService;

    @KafkaListener(topics = EventTopic.REQUESTED_APPOINTMENT_STATE)
    public void listenRequestedAppointmentStateEvent(RequestedAppointmentStateEvent event) {
        log.info("Received requested appointment state event: {}", event);
        appointmentService.updateState(event);
    }
}