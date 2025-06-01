package ru.random.walk.chat_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.random.walk.chat_service.mapper.UserMapper;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.chat_service.service.ChatService;
import ru.random.walk.chat_service.service.UserService;
import ru.random.walk.dto.CreatePrivateChatEvent;
import ru.random.walk.dto.RegisteredUserInfoEvent;
import ru.random.walk.dto.RequestedAppointmentStateEvent;
import ru.random.walk.topic.EventTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    private final ChatService chatService;
    private final AppointmentService appointmentService;
    private final UserService userService;

    @KafkaListener(topics = EventTopic.CREATE_CHAT)
    public void listenCreatePrivateChatEvent(String message) throws JsonProcessingException {
        log.info("Try to handle raw CREATE_CHAT event {}", message);
        var event = objectMapper.readValue(message, CreatePrivateChatEvent.class);
        log.info("Received create chat event: {}", event);
        chatService.create(event);
    }

    @KafkaListener(topics = EventTopic.REQUESTED_APPOINTMENT_STATE)
    public void listenRequestedAppointmentStateEvent(String message) throws JsonProcessingException {
        log.info("Try to handle raw REQUESTED_APPOINTMENT_STATE event {}", message);
        var event = objectMapper.readValue(message, RequestedAppointmentStateEvent.class);
        log.info("Received requested appointment state event: {}", event);
        appointmentService.updateState(event);
    }

    @KafkaListener(topics = EventTopic.USER_REGISTRATION)
    public void listenUserRegistration(String message) throws JsonProcessingException {
        log.info("Try to handle raw USER_REGISTRATION event {}", message);
        var event = objectMapper.readValue(message, RegisteredUserInfoEvent.class);
        log.info("Received registered user info event: {}", event);
        var userEntity = userMapper.toEntity(event);
        userService.add(userEntity);
    }
}