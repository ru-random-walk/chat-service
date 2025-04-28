package ru.random.walk.chat_service;

import org.springframework.web.client.RestTemplate;
import ru.random.walk.chat_service.config.InterConfig;
import ru.random.walk.chat_service.model.dto.matcher.RequestForAppointmentDto;
import ru.random.walk.chat_service.service.auth.impl.AuthTokenProviderImpl;
import ru.random.walk.chat_service.service.client.impl.MatcherClientImpl;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MatcherClientSender {
    public static void main(String[] args) {
        var config = InterConfig.builder()
                .matcher(InterConfig.MatherConfig.builder()
                        .url("http://localhost:8082")
                        .build())
                .auth(InterConfig.AuthConfig.builder()
                        .url("http://localhost:8081")
                        .login("CLUB_TEST_CLIENT")
                        .password("1234Club")
                        .build())
                .build();
        var restTemplate = new RestTemplate();
        var client = new MatcherClientImpl(restTemplate, config, new AuthTokenProviderImpl(config, restTemplate));
        var response = client.requestForAppointment(new RequestForAppointmentDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                OffsetDateTime.now(),
                0d,
                0d
        ));
        System.out.println(response);
    }
}
