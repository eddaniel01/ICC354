package com.ejemplo.carritoservice.controller;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.model.CartItem;
import com.ejemplo.carritoservice.model.Order;
import com.ejemplo.carritoservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{username}")
    public Cart getCart(@PathVariable String username) {
        return cartService.getCartByUser(username);
    }

    @PostMapping("/{username}/add")
    public Cart addItem(@PathVariable String username, @RequestBody CartItem item) {
        return cartService.addItemToCart(username, item);
    }

    @DeleteMapping("/remove/{itemId}")
    public void removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
    }

    @PostMapping("/{username}/confirm")
    public Order confirm(@PathVariable String username) {
        return cartService.confirmOrder(username);
    }

    @GetMapping("/{username}/orders")
    public List<Order> getOrders(@PathVariable String username) {
        return cartService.getOrders(username);
    }
}
