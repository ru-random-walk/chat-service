package ru.random.walk.chat_service.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.chat_service.repository.AppointmentRepository;
import ru.random.walk.chat_service.service.AppointmentService;
import ru.random.walk.dto.RequestedAppointmentStateEvent;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void updateState(RequestedAppointmentStateEvent event) {
        var appointment = appointmentRepository.findByAppointmentId(event.appointmentId()).orElseThrow();
        appointment.setIsAccepted(event.isAccepted());
        appointmentRepository.save(appointment);
    }
}
