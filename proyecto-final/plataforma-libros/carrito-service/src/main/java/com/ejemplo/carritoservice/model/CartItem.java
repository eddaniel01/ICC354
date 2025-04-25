package com.ejemplo.carritoservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookId; // ID del libro desde catalog-service
    private String title;
    private double price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
