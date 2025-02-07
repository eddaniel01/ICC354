package org.example.practica1.service;

import org.example.practica1.model.Estudiante;
import org.example.practica1.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findByActivoTrue();
    }

    public Optional<Estudiante> obtenerEstudiante(Long id) {
        return estudianteRepository.findById(id).filter(Estudiante::isActivo);
    }

    public Estudiante guardarEstudiante(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public Estudiante actualizarEstudiante(Estudiante estudiante) {
        return estudianteRepository.findById(estudiante.getId())
                .map(existingEst -> {
                    existingEst.setMatricula(estudiante.getMatricula());
                    existingEst.setNombre(estudiante.getNombre());
                    existingEst.setApellido(estudiante.getApellido());
                    existingEst.setTelefono(estudiante.getTelefono());
                    return estudianteRepository.save(existingEst);
                }).orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }


    public void eliminarEstudiante(Long id) { // Se cambió int a Long
        estudianteRepository.findById(id).ifPresent(est -> {
            est.setActivo(false);
            estudianteRepository.save(est);
        });
    }
}