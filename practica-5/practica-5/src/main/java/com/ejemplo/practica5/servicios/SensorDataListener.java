package com.ejemplo.practica5.servicios;

import com.ejemplo.practica5.entidades.SensorData;
import com.ejemplo.practica5.servicios.SensorDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SensorDataListener {

    private final SensorDataService sensorDataService;
    private final SimpMessagingTemplate messagingTemplate; // 💡 Inyectamos WebSockets
    private final ObjectMapper objectMapper; // Jackson para manejar JSON

    @Autowired
    public SensorDataListener(SensorDataService sensorDataService, SimpMessagingTemplate messagingTemplate) {
        this.sensorDataService = sensorDataService;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = new ObjectMapper(); // Inicializamos Jackson
    }

    @JmsListener(destination = "notificacion_sensores")
    public void recibirMensaje(String mensaje) {
        System.out.println("📥 Mensaje recibido en JMS: " + mensaje);

        // Convertir JSON a SensorData
        SensorData sensorData = convertirMensajeAObjeto(mensaje);

        // Guardar en la base de datos y enviar a WebSockets si la conversión fue exitosa
        if (sensorData != null) {
            sensorDataService.save(sensorData);
            messagingTemplate.convertAndSend("/topic/datos", sensorData); // 💡 Enviar a WebSockets
            System.out.println("📡 Enviado a WebSocket: " + sensorData);
        }
    }

    private SensorData convertirMensajeAObjeto(String mensaje) {
        try {
            return objectMapper.readValue(mensaje, SensorData.class);
        } catch (Exception e) {
            System.err.println("❌ Error al convertir JSON: " + e.getMessage());
            return null;
        }
    }
}
