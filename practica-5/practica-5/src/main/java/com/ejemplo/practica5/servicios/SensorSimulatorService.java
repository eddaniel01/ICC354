package com.ejemplo.practica5.servicios;

import com.ejemplo.practica5.entidades.SensorData;
import com.ejemplo.practica5.producers.SensorDataProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SensorSimulatorService {

    private final SensorDataProducer sensorDataProducer;

    @Autowired
    public SensorSimulatorService(SensorDataProducer sensorDataProducer) {
        this.sensorDataProducer = sensorDataProducer;
    }

    // Simula el cliente 1 (ID del dispositivo 1)
    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void simularCliente1() {
        SensorData sensorData = new SensorData(1); // ID del dispositivo 1
        sensorDataProducer.enviarMensaje(sensorData);
        System.out.println("📤 Cliente 1 envió datos: " + sensorData);
    }

    // Simula el cliente 2 (ID del dispositivo 2)
    @Scheduled(fixedRate = 60000) // Ejecutar cada minuto
    public void simularCliente2() {
        SensorData sensorData = new SensorData(2); // ID del dispositivo 2
        sensorDataProducer.enviarMensaje(sensorData);
        System.out.println("📤 Cliente 2 envió datos: " + sensorData);
    }
}