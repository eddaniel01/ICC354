package com.ejemplo.authservice.config;

import com.ejemplo.authservice.model.Role;
import com.ejemplo.authservice.model.User;
import com.ejemplo.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createAdminUser() {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "admin123"; // Cambia esto si lo necesitas

            // Si no existe, crear el usuario admin
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("====> Usuario ADMIN creado por defecto. Username: admin, Password: admin123");
            }
        };
    }
}
