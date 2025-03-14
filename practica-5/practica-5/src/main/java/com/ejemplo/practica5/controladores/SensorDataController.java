package com.ejemplo.practica5.controladores;

import com.ejemplo.practica5.entidades.SensorData;
import com.ejemplo.practica5.producers.SensorDataProducer;
import com.ejemplo.practica5.servicios.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensores")
public class SensorDataController {

    private final SensorDataProducer sensorDataProducer;
    private final SensorDataService sensorDataService;

    @Autowired
    public SensorDataController(SensorDataProducer sensorDataProducer, SensorDataService sensorDataService) {
        this.sensorDataProducer = sensorDataProducer;
        this.sensorDataService = sensorDataService;
    }

    @PostMapping("/enviar")
    public String enviarDatos(@RequestBody SensorData sensorData) {
        sensorDataProducer.enviarMensaje(sensorData);
        return "Mensaje enviado a ActiveMQ";
    }

    @GetMapping("/historico/{idDispositivo}")
    public List<SensorData> obtenerHistorial(@PathVariable int idDispositivo) {
        return sensorDataService.findTop10ByIdDispositivoOrderByFechaGeneracionDesc(idDispositivo);
    }

}
