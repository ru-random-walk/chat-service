package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.model.domain.event.RequestForWalkAnswerEvent;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.entity.MessageEntity;
import ru.random.walk.chat_service.repository.AppointmentRepository;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.dto.RequestedAppointmentStateEvent;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public void updateState(RequestedAppointmentStateEvent event) {
        var isAccepted = event.isAccepted();
        var message = attachRequestForWalkAnswer(event, isAccepted);
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), RequestForWalkAnswerEvent.builder()
                .isAccepted(isAccepted)
                .messageId(message.getId())
                .build());
    }

    private MessageEntity attachRequestForWalkAnswer(RequestedAppointmentStateEvent event, Boolean isAccepted) {
        var appointment = appointmentRepository.findByAppointmentId(event.appointmentId()).orElseThrow();
        var message = messageRepository.findById(appointment.getMessageId()).orElseThrow();
        var requestForWalkPayload = (RequestForWalkPayload) message.getPayload();
        requestForWalkPayload.setAnswer(isAccepted);
        message.setPayload(requestForWalkPayload);
        messageRepository.save(message);
        return message;
    }
}
