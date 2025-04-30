package ru.random.walk.chat_service.service;

import ru.random.walk.dto.RequestedAppointmentStateEvent;

public interface AppointmentService {
    void updateState(RequestedAppointmentStateEvent event);
}
