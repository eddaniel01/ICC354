package com.ejemplo.carritoservice.controller;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Ver el carrito por usuario
    @GetMapping("/{username}")
    public ResponseEntity<Cart> getCart(@PathVariable String username) {
        return ResponseEntity.ok(cartService.getCartByUsername(username));
    }

    // Agregar un ítem al carrito
    @PostMapping("/{username}/add")
    public ResponseEntity<Cart> addItemToCart(
            @PathVariable String username,
            @RequestParam String bookId,
            @RequestParam String title,
            @RequestParam double price
    ) {
        return ResponseEntity.ok(cartService.addItem(username, bookId, title, price));
    }

    // Eliminar un ítem por ID
    @DeleteMapping("/{username}/remove/{itemId}")
    public ResponseEntity<Cart> removeItem(
            @PathVariable String username,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(cartService.removeItem(username, itemId));
    }

    // Vaciar carrito
    @DeleteMapping("/{username}/clear")
    public ResponseEntity<Cart> clearCart(@PathVariable String username) {
        return ResponseEntity.ok(cartService.clearCart(username));
    }
}
