package com.ejemplo.practica2.service;

import com.ejemplo.practica2.model.Role;
import com.ejemplo.practica2.model.User;
import com.ejemplo.practica2.repository.RoleRepository;
import com.ejemplo.practica2.repository.UserRepository;
import com.ejemplo.practica2.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String registerUser(String username, String password, boolean isAdmin) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        Set<Role> roles = new HashSet<>();
        String roleName = isAdmin ? "ADMIN" : "USER";
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("El rol no existe"));
        roles.add(role);

        User newUser = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        userRepository.save(newUser);
        return jwtUtil.generateToken(username);
    }

    public String authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return jwtUtil.generateToken(username);
    }

}
