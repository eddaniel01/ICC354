package com.ejemplo.practica5.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Entity
@Table(name="sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("fechaGeneracion")
    private String fechaGeneracion;

    @JsonProperty("idDispositivo")
    private int idDispositivo;

    @JsonProperty("temperatura")
    private double temperatura;

    @JsonProperty("humedad")
    private double humedad;

    public SensorData(int idDispositivo) {
        this.fechaGeneracion = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.idDispositivo = idDispositivo;
        this.temperatura = new Random().nextDouble() * 30 + 10; // Entre 10 y 40 grados
        this.humedad = new Random().nextDouble() * 50 + 20; // Entre 20% y 70%
    }

    public SensorData() {}

    // Getters y Setters
    public String getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(String fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public int getIdDispositivo() { return idDispositivo; }
    public void setIdDispositivo(int idDispositivo) { this.idDispositivo = idDispositivo; }

    public double getTemperatura() { return temperatura; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }

    public double getHumedad() { return humedad; }
    public void setHumedad(double humedad) { this.humedad = humedad; }

    @Override
    public String toString() {
        return String.format("{\"fechaGeneracion\":\"%s\", \"idDispositivo\":%d, \"temperatura\":%.2f, \"humedad\":%.2f}",
                fechaGeneracion, idDispositivo, temperatura, humedad);
    }
}
