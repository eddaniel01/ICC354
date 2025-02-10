package com.ejemplo.practica2.controller;

import com.ejemplo.practica2.model.MockEndpoint;
import com.ejemplo.practica2.model.User;
import com.ejemplo.practica2.repository.UserRepository;
import com.ejemplo.practica2.security.CustomUserDetailsService;
import com.ejemplo.practica2.security.JwtUtil;
import com.ejemplo.practica2.service.MockEndpointService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mocks")
@RequiredArgsConstructor
public class MockEndpointController {

    private final MockEndpointService service;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    // Crear Mock
    @PostMapping("/create")
    public ResponseEntity<MockEndpoint> createMock(
            @RequestBody MockEndpoint mock,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(service.createMock(mock, user));
    }

    // Listar Mocks del usuario autenticado
    @GetMapping("/my-mocks")
    public ResponseEntity<List<MockEndpoint>> getUserMocks(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(service.getMocksByUser(user));
    }

    // Solo ADMIN puede ver todos los mocks
    @GetMapping("/all")
    public ResponseEntity<List<MockEndpoint>> getAllMocks() {
        return ResponseEntity.ok(service.getAllMocks());
    }

    // Eliminar Mock
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMock(@PathVariable Long id) {
        service.deleteMock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mock/**")
    public ResponseEntity<?> handleMockRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        Optional<MockEndpoint> mockOpt = service.findByPath(path);

        if (mockOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Mock no encontrado");
        }

        MockEndpoint mock = mockOpt.get();

        if (mock.isJwtProtected()) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(403).body("Acceso denegado: Token requerido");
            }
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token, customUserDetailsService.loadUserByUsername(jwtUtil.extractUsername(token)))) {
                return ResponseEntity.status(403).body("Acceso denegado: Token inválido");
            }
        }

        if (mock.getExpirationDate() != null && mock.getExpirationDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(410).body("El mock ha expirado.");
        }

        return ResponseEntity
                .status(mock.getResponseCode())
                .header("Content-Type", mock.getContentType())
                .body(mock.getResponseBody());
    }
}