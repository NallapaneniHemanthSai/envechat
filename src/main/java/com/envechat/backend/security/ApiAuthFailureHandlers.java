package com.envechat.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps unauthenticated access to protected REST APIs to 401 instead of the default 403,
 * so clients can distinguish "missing/invalid session" from "forbidden for this principal".
 */
@Component
@RequiredArgsConstructor
public class ApiAuthFailureHandlers implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    static boolean isProtectedApiPath(String uri) {
        if (uri == null) {
            return false;
        }
        return uri.startsWith("/api/") && !uri.startsWith("/api/auth");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (isProtectedApiPath(request.getRequestURI())) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", request.getRequestURI());
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String uri = request.getRequestURI();
        if (!isProtectedApiPath(uri)) {
            writeJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", uri);
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean insufficient = accessDeniedException.getCause() instanceof InsufficientAuthenticationException;
        boolean anonymous = auth == null || auth instanceof AnonymousAuthenticationToken;

        if (insufficient || anonymous) {
            writeJson(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized", uri);
            return;
        }

        writeJson(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden", uri);
    }

    private void writeJson(HttpServletResponse response, int status, String error, String path)
            throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", status);
        body.put("error", error);
        body.put("path", path);

        objectMapper.writeValue(response.getWriter(), body);
    }
}
