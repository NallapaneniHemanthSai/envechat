package com.envechat.backend.controller;

import com.envechat.backend.dto.AuthResponse;
import com.envechat.backend.dto.LoginRequest;
import com.envechat.backend.dto.SignupRequest;
import com.envechat.backend.model.User;
import com.envechat.backend.service.AuthService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://envechat.onrender.com"
})
public class AuthController {

    private final AuthService authService;

    // 🔐 Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request
    ) {

        User user = authService.signup(request);

        return ResponseEntity.ok(user);
    }

    // 🔐 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}