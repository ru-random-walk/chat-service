package ru.random.walk.chat_service.service.client;

import ru.random.walk.chat_service.model.dto.matcher.AppointmentDetailsDto;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;

public interface MatcherClient {
    AppointmentDetailsDto requestForAppointment(RequestForAppointmentDto dto);
}
