package com.ejemplo.practica5.controladores;

import com.ejemplo.practica5.entidades.SensorData;
import com.ejemplo.practica5.producers.SensorDataProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensores")
public class SensorDataController {

    private final SensorDataProducer sensorDataProducer;

    @Autowired
    public SensorDataController(SensorDataProducer sensorDataProducer) {
        this.sensorDataProducer = sensorDataProducer;
    }

    @PostMapping("/enviar")
    public String enviarDatos(@RequestBody SensorData sensorData) {
        sensorDataProducer.enviarMensaje(sensorData);
        return "Mensaje enviado a ActiveMQ";
    }

}
