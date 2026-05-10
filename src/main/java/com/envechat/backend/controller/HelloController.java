package com.envechat.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Envechat backend running successfully";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}