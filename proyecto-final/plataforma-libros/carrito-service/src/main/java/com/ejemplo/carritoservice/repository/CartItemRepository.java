package com.ejemplo.carritoservice.repository;

import com.ejemplo.carritoservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
