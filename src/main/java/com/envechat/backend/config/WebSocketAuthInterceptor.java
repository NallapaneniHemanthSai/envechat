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

            String authHeader = extractAuthHeader(accessor);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("WebSocket CONNECT rejected: Missing/invalid Authorization header");
                return null; // DROP frame (don’t crash handshake)
            }

            String token = authHeader.substring(7);

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
     * Extract Authorization header safely (handles tool inconsistencies)
     */
    private String extractAuthHeader(StompHeaderAccessor accessor) {

        // Try standard header
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            return authHeaders.get(0);
        }

        // Try lowercase (Postman/WebSocket tools sometimes use this)
        List<String> altHeaders = accessor.getNativeHeader("authorization");
        if (altHeaders != null && !altHeaders.isEmpty()) {
            return altHeaders.get(0);
        }

        return null;
    }
}