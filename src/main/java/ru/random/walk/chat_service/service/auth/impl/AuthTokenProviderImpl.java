package ru.random.walk.chat_service.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.random.walk.chat_service.config.InterConfig;
import ru.random.walk.chat_service.model.dto.auth.TokenResponse;
import ru.random.walk.chat_service.service.auth.AuthTokenProvider;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthTokenProviderImpl implements AuthTokenProvider {
    private final InterConfig interConfig;
    private final RestTemplate restTemplate;

    private String token;
    private LocalDateTime expiredTime;

    @Override
    public String getToken() {
        if (Objects.isNull(token)
                || Objects.isNull(expiredTime)
                || expiredTime.isBefore(LocalDateTime.now())
        ) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(interConfig.auth().login(), interConfig.auth().password());

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            TokenResponse response = restTemplate.postForEntity(
                    interConfig.auth().url() + "/token",
                    request,
                    TokenResponse.class
            ).getBody();

            if (Objects.isNull(response)) {
                throw new RuntimeException("Failed to retrieve token from auth!");
            }

            expiredTime = LocalDateTime.now().plusSeconds(response.expiresIn());
            token = response.accessToken();
        }
        return token;
    }
}
