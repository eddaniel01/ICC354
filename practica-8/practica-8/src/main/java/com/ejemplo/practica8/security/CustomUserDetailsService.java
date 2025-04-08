package com.ejemplo.practica8.security;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.repository.GerenteRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final GerenteRepository repository;

    public CustomUserDetailsService(GerenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("🧠 Intentando autenticar al correo: " + correo);

        Gerente gerente = repository.findAll().stream()
                .filter(g -> g.getCorreo().equalsIgnoreCase(correo.trim()))
                .findFirst()
                .orElseThrow(() -> {
                    System.out.println("❌ Gerente no encontrado");
                    return new UsernameNotFoundException("Correo no registrado: " + correo);
                });

        System.out.println("✅ Gerente encontrado: " + gerente.getCorreo());
        System.out.println("🔑 Contraseña registrada: " + gerente.getPassword());

        return User.builder()
                .username(gerente.getCorreo())
                .password(gerente.getPassword())
                .roles(gerente.getRol().toUpperCase()) // Usar el rol guardado en la DB
                .build();

    }
}
