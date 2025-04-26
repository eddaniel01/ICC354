package com.ejemplo.carritoservice.repository;

import com.ejemplo.carritoservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUsername(String username);

    // encontrar todos los carritos de un usuario (por seguridad)
    List<Cart> findAllByUsername(String username);
}
