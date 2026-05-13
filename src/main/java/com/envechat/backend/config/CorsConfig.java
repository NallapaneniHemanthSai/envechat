package com.envechat.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(@Value("${CORS_ALLOWED_ORIGINS:}") String corsAllowedOriginsEnv) {

        CorsConfiguration config = new CorsConfiguration();

        List<String> allowedOrigins = CorsOrigins.fromEnvOrDefaults(corsAllowedOriginsEnv);
        config.setAllowedOrigins(allowedOrigins);

        // Allow credentials (JWT/cookies/auth headers)
        config.setAllowCredentials(true);

        // Allow all headers
        config.setAllowedHeaders(List.of("*"));

        // Allow all HTTP methods
        config.setAllowedMethods(List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS"
        ));

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}