package com.ejemplo.practica8.service;

import com.ejemplo.practica8.model.Evento;
import java.util.List;

public interface EventoService {
    List<Evento> findAll();
    List<Evento> findByGerenteId(Long gerenteId);
    Evento save(Evento evento);
    void delete(Long id);
}
