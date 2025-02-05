package org.example;

import java.util.*;

public class EstudianteDAO {
    private static Map<String, Estudiante> estudiantes = new HashMap<>();

    static {
        estudiantes.put("2001-1136", new Estudiante("2001-1136", "Carlos", "Camacho", "8123123123"));
    }

    public static List<Estudiante> listarEstudiantes() {
        return new ArrayList<>(estudiantes.values());
    }

    public static Estudiante obtenerEstudiante(String matricula) {
        return estudiantes.get(matricula);
    }

    public static void agregarEstudiante(Estudiante estudiante) {
        System.out.println("Guardando estudiante: " + estudiante.getMatricula()); // DEBUG
        estudiantes.put(estudiante.getMatricula(), estudiante);
    }

    public static void actualizarEstudiante(String matricula, Estudiante nuevo) {
        estudiantes.put(matricula, nuevo);
    }

    public static void eliminarEstudiante(String matricula) {
        estudiantes.remove(matricula);
    }
}
