package com.ejemplo.practica2.controller;

import com.ejemplo.practica2.model.MockEndpoint;
import com.ejemplo.practica2.service.MockEndpointService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PublicMockController {

    private final MockEndpointService service;

    @GetMapping("/mock/**")  // Solo una vez "mock"
    public ResponseEntity<?> handleMockRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        Optional<MockEndpoint> mockOpt = service.findByPath(path);

        if (mockOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Mock no encontrado");
        }

        MockEndpoint mock = mockOpt.get();

        if (mock.getExpirationDate() != null && mock.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(410).body("El mock ha expirado.");
        }

        return ResponseEntity
                .status(mock.getResponseCode())
                .header("Content-Type", mock.getContentType())
                .body(mock.getResponseBody());
    }
}