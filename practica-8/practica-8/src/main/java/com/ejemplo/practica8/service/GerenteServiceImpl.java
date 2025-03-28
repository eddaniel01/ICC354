package com.ejemplo.practica8.service;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.repository.GerenteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GerenteServiceImpl implements GerenteService {

    private final GerenteRepository repository;

    public GerenteServiceImpl(GerenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Gerente> findAll(int offset, int limit) {
        return repository.findAll(PageRequest.of(offset / limit, limit)).getContent();
    }

    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public void save(Gerente gerente) {
        repository.save(gerente);
    }

    @Override
    public void delete(Gerente gerente) {
        repository.delete(gerente);
    }
}
