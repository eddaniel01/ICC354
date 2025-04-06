package com.ejemplo.practica8.repository;

import com.ejemplo.practica8.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByGerenteId(Long gerenteId);
}
