package ru.random.walk.chat_service.service.client.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.random.walk.chat_service.config.InterConfig;
import ru.random.walk.chat_service.model.dto.matcher.AppointmentDetailsDto;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;
import ru.random.walk.chat_service.service.auth.AuthTokenProvider;
import ru.random.walk.chat_service.service.client.MatcherClient;

@Service
@AllArgsConstructor
public class MatcherClientImpl implements MatcherClient {
    private final RestTemplate restTemplate;
    private final InterConfig interConfig;
    private final AuthTokenProvider authTokenProvider;

    @Override
    public AppointmentDetailsDto requestForAppointment(RequestForAppointmentDto dto) {
        String token = authTokenProvider.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<RequestForAppointmentDto> request = new HttpEntity<>(dto, headers);
        ResponseEntity<AppointmentDetailsDto> response = restTemplate.postForEntity(
                interConfig.matcher().url() + "/internal/appointment/request",
                request,
                AppointmentDetailsDto.class
        );
        return response.getBody();
    }
}
