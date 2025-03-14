package com.ejemplo.practica5.producers;

import com.ejemplo.practica5.entidades.SensorData;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SensorDataProducer {

    private final JmsTemplate jmsTemplate;

    public SensorDataProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void enviarMensaje(SensorData sensorData) {
        jmsTemplate.convertAndSend("notificacion_sensores", sensorData.toString());
        System.out.println("Mensaje enviado a ActiveMQ: " + sensorData);
    }
}
