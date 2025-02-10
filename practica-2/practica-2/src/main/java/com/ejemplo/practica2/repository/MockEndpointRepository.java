package com.ejemplo.practica2.repository;

import com.ejemplo.practica2.model.MockEndpoint;
import com.ejemplo.practica2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MockEndpointRepository extends JpaRepository<MockEndpoint, Long> {
    List<MockEndpoint> findByOwner(User owner);    // Mocks por usuario
    List<MockEndpoint> findByExpirationDateAfter(LocalDateTime now); // Mocks no expirados
    Optional<MockEndpoint> findByPath(String path);
}