package com.envechat.backend.config;

import com.envechat.backend.security.ApiAuthFailureHandlers;
import com.envechat.backend.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ApiAuthFailureHandlers apiAuthFailureHandlers;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(apiAuthFailureHandlers)
                    .accessDeniedHandler(apiAuthFailureHandlers)
            )

            .authorizeHttpRequests(auth -> auth

                // Allow preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Public endpoints
                .requestMatchers(
                        "/",
                        "/health",
                        "/error",
                        "/api/auth/**",
                        "/ws/**"
                ).permitAll()

                // Public room browse (read-only)
                .requestMatchers(HttpMethod.GET, "/api/rooms").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/rooms/*").permitAll()

                // Everything else requires auth
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                    jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}