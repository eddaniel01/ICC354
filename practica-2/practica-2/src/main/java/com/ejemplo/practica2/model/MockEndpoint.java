package com.ejemplo.practica2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "mock_endpoints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockEndpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;                   // Ruta del endpoint
    private String method;                 // GET, POST, PUT, DELETE
    private int responseCode;
    private String contentType;
    @Column(length = 5000)
    private String responseBody;
    private LocalDateTime expirationDate;
    private int delayInSeconds;
    private boolean jwtProtected;

    private String name;
    private String description;

    @ElementCollection
    private Map<String, String> headers;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}