package com.ejemplo.practica2.config;

import com.ejemplo.practica2.model.User;
import com.ejemplo.practica2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123")) // 👈 Contraseña encriptada
                    .roles(Set.of("ADMIN")) // 👈 Asegúrate de que coincide con los roles de seguridad
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado con éxito (admin/admin123)");
        }
    }
}