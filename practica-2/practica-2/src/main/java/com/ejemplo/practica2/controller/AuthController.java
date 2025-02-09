package com.ejemplo.practica2.controller;

import com.ejemplo.practica2.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        String token = authService.registerUser(request.get("username"), request.get("password"), Boolean.parseBoolean(request.get("isAdmin")));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String token = authService.authenticateUser(request.get("username"), request.get("password"));
        return ResponseEntity.ok(Map.of("token", token));
    }

}
