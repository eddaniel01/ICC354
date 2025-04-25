package com.ejemplo.carritoservice.controller;

import com.ejemplo.carritoservice.model.Order;
import com.ejemplo.carritoservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{username}")
    public ResponseEntity<Order> createOrder(@PathVariable String username) {
        return ResponseEntity.ok(orderService.createOrder(username));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Order>> getOrders(@PathVariable String username) {
        return ResponseEntity.ok(orderService.getOrdersByUsername(username));
    }
}
