package com.ejemplo.practica2.controller;

import com.ejemplo.practica2.model.MockAPI;
import com.ejemplo.practica2.model.Usuario;
import com.ejemplo.practica2.service.MockAPIService;
import com.ejemplo.practica2.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mock")
@Validated
public class MockAPIController {

    private final MockAPIService mockAPIService;
    private final UsuarioService usuarioService;

    public MockAPIController(MockAPIService mockAPIService, UsuarioService usuarioService) {
        this.mockAPIService = mockAPIService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<MockAPI>> listarMockAPIs(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.obtenerUsuario(userDetails.getUsername());
        return ResponseEntity.ok(mockAPIService.listarMockAPIs(usuario));
    }

    @PostMapping
    public ResponseEntity<MockAPI> crearMockAPI(@AuthenticationPrincipal UserDetails userDetails,
                                                @Valid @RequestBody MockAPI mockAPI) {
        Usuario usuario = usuarioService.obtenerUsuario(userDetails.getUsername());
        return ResponseEntity.ok(mockAPIService.crearMockAPI(mockAPI, usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MockAPI> obtenerMockAPI(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.obtenerUsuario(userDetails.getUsername());
        MockAPI mockAPI = mockAPIService.obtenerMockAPI(id, usuario)
                .orElseThrow(() -> new RuntimeException("Mock API no encontrada"));

        return ResponseEntity.ok(mockAPI);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMockAPI(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.obtenerUsuario(userDetails.getUsername());
        mockAPIService.eliminarMockAPI(id, usuario);
        return ResponseEntity.noContent().build();
    }
}
