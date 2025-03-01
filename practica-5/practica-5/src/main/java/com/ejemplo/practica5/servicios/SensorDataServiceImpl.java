package com.ejemplo.practica5.servicios;

import com.ejemplo.practica5.entidades.SensorData;
import com.ejemplo.practica5.repositorios.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataRepository repository;

    @Autowired
    public SensorDataServiceImpl(SensorDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SensorData> findAllByIdDispositivo(int idDispositivo) {
        return repository.findAllByIdDispositivo(idDispositivo);
    }

    @Override
    public SensorData save(SensorData sensorData) {
        return repository.save(sensorData);
    }
}
