package org.example.practica2.repository;

import org.example.practica2.model.Estudiante;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EstudianteRepository {

    private static List<Estudiante> estudiantes= new ArrayList<>();
    private static int idCounter = estudiantes.isEmpty() ? 1 : estudiantes.get(estudiantes.size() - 1).getMatricula() + 1;


    public List<Estudiante> obtenerTodos(){
        return estudiantes;
    }

    public Estudiante obtenerPorMatricula(int matricula) {
        return estudiantes.stream().filter(e -> e.getMatricula() == matricula).findFirst().orElse(null);
    }


    public void agregar(Estudiante estudiante){
        estudiante.setMatricula(idCounter++);
        estudiantes.add(estudiante);
    }

    public void actualizar(Estudiante estudiante) {
        for (int i = 0; i < estudiantes.size(); i++) {
            if (estudiantes.get(i).getMatricula() == estudiante.getMatricula()) {
                estudiantes.set(i, estudiante);  // Se reemplaza el estudiante en la lista
                return;
            }
        }
    }


    public void eliminar(int matricula) {
        estudiantes.removeIf(e -> e.getMatricula() == matricula);
    }
}
