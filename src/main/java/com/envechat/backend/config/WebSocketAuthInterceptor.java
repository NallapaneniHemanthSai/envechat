package com.envechat.backend.config;

import com.envechat.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        // Only authenticate during CONNECT
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = extractJwtToken(accessor);

            if (token == null || token.isBlank()) {
                log.warn("WebSocket CONNECT rejected: Missing JWT (Authorization, access_token, or token)");
                return null; // DROP frame (don’t crash handshake)
            }

            if (!jwtUtil.validateToken(token)) {
                log.warn("WebSocket CONNECT rejected: Invalid token");
                return null;
            }

            String email = jwtUtil.extractEmail(token);
            if (email == null) {
                log.warn("WebSocket CONNECT rejected: Email not found in token");
                return null;
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            accessor.setUser(authentication);

            log.info("WebSocket authenticated successfully: {}", email);
        }

        return message;
    }

    /**
     * Raw JWT from STOMP CONNECT: {@code Authorization: Bearer ...}, {@code access_token}, or {@code token}.
     */
    private String extractJwtToken(StompHeaderAccessor accessor) {
        String authorization = firstNativeHeader(accessor, "Authorization");
        if (authorization != null && !authorization.isBlank()) {
            String v = authorization.trim();
            if (v.regionMatches(true, 0, "Bearer ", 0, 7)) {
                return v.substring(7).trim();
            }
            return v;
        }

        String accessToken = firstNativeHeader(accessor, "access_token");
        if (accessToken != null && !accessToken.isBlank()) {
            return accessToken.trim();
        }

        String token = firstNativeHeader(accessor, "token");
        if (token != null && !token.isBlank()) {
            return token.trim();
        }

        return null;
    }

    private String firstNativeHeader(StompHeaderAccessor accessor, String name) {
        List<String> values = accessor.getNativeHeader(name);
        if (values != null && !values.isEmpty()) {
            return values.get(0);
        }
        if (!name.isEmpty()) {
            char c = name.charAt(0);
            char toggled = Character.isUpperCase(c)
                    ? Character.toLowerCase(c)
                    : Character.toUpperCase(c);
            if (c != toggled) {
                String alt = toggled + name.substring(1);
                List<String> altValues = accessor.getNativeHeader(alt);
                if (altValues != null && !altValues.isEmpty()) {
                    return altValues.get(0);
                }
            }
        }
        return null;
    }
}