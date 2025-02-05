package org.example;

import com.google.gson.Gson;
import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        staticFiles.location("/public"); // Carpeta de archivos HTML y CSS

        Gson gson = new Gson();

        // Ruta para listar los estudiantes en JSON (para el fetch de index.html)
        get("/estudiantes/listar", (req, res) -> {
            res.type("application/json");
            return gson.toJson(EstudianteDAO.listarEstudiantes());
        });

        // Insertar un nuevo estudiante
        post("/estudiantes", (req, res) -> {
            String matricula = req.queryParams("matricula");
            String nombre = req.queryParams("nombre");
            String apellido = req.queryParams("apellido");
            String telefono = req.queryParams("telefono");

            if (!EstudianteDAO.listarEstudiantes().stream().anyMatch(e -> e.getMatricula().equals(matricula))) {
                EstudianteDAO.agregarEstudiante(new Estudiante(matricula, nombre, apellido, telefono));
            }
            res.redirect("/index.html");
            return null;
        });

        // Cargar datos de un estudiante en el formulario de edición
        get("/estudiantes/editar", (req, res) -> {
            String matricula = req.queryParams("matricula");
            Estudiante estudiante = EstudianteDAO.obtenerEstudiante(matricula);

            if (estudiante != null) {
                res.redirect("/editar.html?matricula=" + estudiante.getMatricula() +
                        "&nombre=" + estudiante.getNombre() +
                        "&apellido=" + estudiante.getApellido() +
                        "&telefono=" + estudiante.getTelefono());
            } else {
                res.redirect("/index.html");
            }
            return null;
        });

        // Actualizar estudiante
        post("/estudiantes/actualizar", (req, res) -> {
            String matricula = req.queryParams("matricula");
            String nombre = req.queryParams("nombre");
            String apellido = req.queryParams("apellido");
            String telefono = req.queryParams("telefono");

            if (EstudianteDAO.obtenerEstudiante(matricula) != null) {
                EstudianteDAO.actualizarEstudiante(matricula, new Estudiante(matricula, nombre, apellido, telefono));
            }

            res.redirect("/index.html");
            return null;
        });

        // Eliminar estudiante
        post("/estudiantes/eliminar", (req, res) -> {
            String matricula = req.queryParams("matricula");
            EstudianteDAO.eliminarEstudiante(matricula);
            res.redirect("/index.html");
            return null;
        });
    }
}
