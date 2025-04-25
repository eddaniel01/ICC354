package com.ejemplo.carritoservice.service;

import com.ejemplo.carritoservice.model.Cart;
import com.ejemplo.carritoservice.model.CartItem;
import com.ejemplo.carritoservice.model.Order;
import com.ejemplo.carritoservice.repository.CartItemRepository;
import com.ejemplo.carritoservice.repository.CartRepository;
import com.ejemplo.carritoservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public Order createOrder(String username) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        double total = cart.getItems().stream().mapToDouble(CartItem::getPrice).sum();

        Order order = Order.builder()
                .username(username)
                .totalAmount(total)
                .build();

        order = orderRepository.save(order);

        cart.getItems().clear();
        cartItemRepository.deleteAll();
        cartRepository.save(cart);

        return order;
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }
}
