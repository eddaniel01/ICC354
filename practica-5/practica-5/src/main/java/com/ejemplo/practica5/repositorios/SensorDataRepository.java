package com.ejemplo.practica5.repositorios;

import com.ejemplo.practica5.entidades.SensorData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {
    List<SensorData> findAllByIdDispositivo(int idDispositivo);
    List<SensorData> findTop10ByIdDispositivoOrderByFechaGeneracionDesc(int idDispositivo, Pageable pageable);
}
