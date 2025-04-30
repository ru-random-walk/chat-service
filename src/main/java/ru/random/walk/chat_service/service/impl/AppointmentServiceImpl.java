package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.model.domain.payload.RequestForWalkPayload;
import ru.random.walk.chat_service.model.entity.AppointmentEntity;
import ru.random.walk.chat_service.repository.AppointmentRepository;
import ru.random.walk.chat_service.repository.MessageRepository;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.dto.RequestedAppointmentStateEvent;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public void updateState(RequestedAppointmentStateEvent event) {
        var appointment = appointmentRepository.findByAppointmentId(event.appointmentId()).orElseThrow();
        appointment.setIsAccepted(event.isAccepted());
        appointmentRepository.save(appointment);
        attachAnswerForRequestForWalkMessagePayload(event, appointment);
    }

    private void attachAnswerForRequestForWalkMessagePayload(
            RequestedAppointmentStateEvent event,
            AppointmentEntity appointment
    ) {
        var message = messageRepository.findById(appointment.getMessageId()).orElseThrow();
        var requestForWalkPayload = (RequestForWalkPayload) message.getPayload();
        requestForWalkPayload.setAnswer(event.isAccepted());
        message.setPayload(requestForWalkPayload);
        messageRepository.save(message);
    }
}
