package com.ejemplo.practica5.servicios;

import com.ejemplo.practica5.entidades.SensorData;
import java.util.List;

public interface SensorDataService {
    List<SensorData> findAllByIdDispositivo(int idDispositivo);
    SensorData save(SensorData sensorData);
}
