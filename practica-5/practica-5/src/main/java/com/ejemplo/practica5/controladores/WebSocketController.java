package com.ejemplo.practica5.controladores;

import com.ejemplo.practica5.entidades.SensorData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sensores")
    @SendTo("/topic/datos")
    public SensorData enviarDatos(SensorData sensorData) {
        return sensorData; // Envía los datos a todos los clientes suscritos a "/topic/datos"
    }
}
