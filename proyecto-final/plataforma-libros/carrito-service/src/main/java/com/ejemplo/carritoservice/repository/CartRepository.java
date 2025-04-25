package com.ejemplo.carritoservice.repository;

import com.ejemplo.carritoservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUsername(String username);
}
