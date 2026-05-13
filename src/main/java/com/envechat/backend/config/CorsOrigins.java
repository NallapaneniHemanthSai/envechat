package com.envechat.backend.config;

import java.util.Arrays;
import java.util.List;

final class CorsOrigins {

    private CorsOrigins() {
    }

    static List<String> fromEnvOrDefaults(String corsAllowedOriginsEnv) {
        if (corsAllowedOriginsEnv == null || corsAllowedOriginsEnv.isBlank()) {
            return List.of(
                    "http://localhost:5173",
                    "https://envechat-frontend.vercel.app"
            );
        }
        return Arrays.stream(corsAllowedOriginsEnv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
