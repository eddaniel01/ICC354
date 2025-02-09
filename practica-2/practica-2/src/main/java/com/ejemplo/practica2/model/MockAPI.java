package com.ejemplo.practica2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "mock_api")
@Getter
@Setter  // ✅ Agrega esta anotación para generar automáticamente los getters y setters
@NoArgsConstructor
@AllArgsConstructor
public class MockAPI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El endpoint no puede estar vacío")
    private String endpoint;

    @Enumerated(EnumType.STRING)
    private HttpMethod method;

    @ElementCollection
    @CollectionTable(name = "mock_headers", joinColumns = @JoinColumn(name = "mock_id"))
    @MapKeyColumn(name = "header_key")
    @Column(name = "header_value")
    private Map<String, String> headers;

    @NotNull(message = "El código de respuesta es obligatorio")
    private Integer responseCode;

    @NotBlank(message = "El tipo de contenido no puede estar vacío")
    private String contentType;

    @Lob
    private String responseBody;

    private String nombre;
    private String descripcion;

    @NotNull
    private LocalDateTime expirationDate;

    private Integer delaySeconds;

    private boolean requireJwt;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;  // ✅ Asegúrate de que este campo está definido correctamente

}
