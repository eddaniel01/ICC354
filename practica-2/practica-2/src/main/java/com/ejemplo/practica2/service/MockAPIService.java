package com.ejemplo.practica2.service;

import com.ejemplo.practica2.model.MockAPI;
import com.ejemplo.practica2.model.Rol;
import com.ejemplo.practica2.model.Usuario;
import com.ejemplo.practica2.repository.MockAPIRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MockAPIService {

    private final MockAPIRepository mockAPIRepository;

    public MockAPIService(MockAPIRepository mockAPIRepository) {
        this.mockAPIRepository = mockAPIRepository;
    }

    public List<MockAPI> listarMockAPIs(Usuario usuario) {
        if (usuario.getRol() == Rol.ADMIN) {
            return mockAPIRepository.findAll(); // Los administradores pueden ver todas las Mock APIs
        }
        return mockAPIRepository.findByUsuario(usuario); // Usuarios normales solo ven sus Mock APIs
    }

    public Optional<MockAPI> obtenerMockAPI(Long id, Usuario usuario) {
        Optional<MockAPI> mockAPI = mockAPIRepository.findById(id);

        if (mockAPI.isPresent()) {
            if (!mockAPI.get().getUsuario().equals(usuario) && usuario.getRol() != Rol.ADMIN) {
                throw new AccessDeniedException("No tienes permiso para ver esta Mock API.");
            }
        }

        return mockAPI;
    }

    public MockAPI crearMockAPI(MockAPI mockAPI, Usuario usuario) {
        mockAPI.setUsuario(usuario); // Asociamos el Mock API al usuario creador
        return mockAPIRepository.save(mockAPI);
    }

    public void eliminarMockAPI(Long id, Usuario usuario) {
        MockAPI mockAPI = mockAPIRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mock API no encontrada"));

        if (!mockAPI.getUsuario().equals(usuario) && usuario.getRol() != Rol.ADMIN) {
            throw new AccessDeniedException("No tienes permiso para eliminar esta Mock API.");
        }

        mockAPIRepository.deleteById(id);
    }
}
