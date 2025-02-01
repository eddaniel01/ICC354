package org.example.practica2.service;

import org.example.practica2.model.Estudiante;
import org.example.practica2.repository.EstudianteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    private final EstudianteRepository repository;

    public EstudianteService(EstudianteRepository repository) {
        this.repository = repository;
    }

    public List<Estudiante> obtenerTodos(){
        return repository.obtenerTodos();
    }

    public Estudiante obtenerPorMatricula(int matricula) {
        return repository.obtenerPorMatricula(matricula);
    }

    public void agregar (Estudiante estudiante){
        repository.agregar(estudiante);
    }

    public void actualizar (Estudiante estudiante){
        repository.actualizar(estudiante);
    }

    public void eliminar(int matricula){
        repository.eliminar(matricula);
    }
}
