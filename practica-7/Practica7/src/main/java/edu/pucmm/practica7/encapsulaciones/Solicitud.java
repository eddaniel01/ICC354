package edu.pucmm.practica7.encapsulaciones;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*; // Importar anotaciones de AWS SDK v2
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@DynamoDbBean // Anotación para DynamoDB en AWS SDK v2
@Getter
@Setter
@NoArgsConstructor
public class Solicitud {

    private String id;
    private String matricula;
    private String nombre;
    private String correo;
    private String laboratorio;
    private String fecha;
    private String hora;

    @DynamoDbPartitionKey // 🔹 Indica que este es el campo clave primaria
    @DynamoDbAttribute("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        // Si no se proporciona un ID, genera uno automáticamente
        this.id = (id == null || id.isEmpty()) ? UUID.randomUUID().toString() : id;
    }

    @DynamoDbAttribute("matricula")
    public String getMatricula() {
        return matricula;
    }

    @DynamoDbAttribute("nombre")
    public String getNombre() {
        return nombre;
    }

    @DynamoDbAttribute("correo")
    public String getCorreo() {
        return correo;
    }

    @DynamoDbAttribute("laboratorio")
    public String getLaboratorio() {
        return laboratorio;
    }

    @DynamoDbAttribute("fecha")
    public String getFecha() {
        return fecha;
    }

    @DynamoDbAttribute("hora")
    public String getHora() {
        return hora;
    }
}