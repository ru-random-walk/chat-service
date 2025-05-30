package ru.random.walk.chat_service.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    public static final String[] SWAGGER_REQUEST_MATCHERS = {
            // Swagger
            "/configuration/security",
            "/configuration/ui",
            "/swagger-resources/**",
            "/swagger-resources",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
    };
    public static final String[] AUTH_WHITELIST = {
            // Spring boot actuator
            "/actuator/**"
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(SWAGGER_REQUEST_MATCHERS).hasAnyAuthority("TESTER", "ADMIN")
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                jwt -> {
                    List<String> list = jwt.getClaimAsStringList("authorities");
                    return list.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                }
        );
        return converter;
    }
}
