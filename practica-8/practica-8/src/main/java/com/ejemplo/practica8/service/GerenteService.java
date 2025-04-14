package com.ejemplo.practica8.service;

import com.ejemplo.practica8.model.Gerente;

import java.util.List;

public interface GerenteService {
    List<Gerente> findAll(int offset, int limit);
    int count();
    void save(Gerente gerente);
    void delete(Gerente gerente);
    Gerente findByCorreo(String correo);

}
