package com.ejemplo.practica3.controller;

import com.ejemplo.practica3.model.Estudiante;
import com.ejemplo.practica3.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public String listarEstudiantes(Model model) {
        List<Estudiante> estudiantes = estudianteService.listarEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "listar";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        Estudiante nuevoEstudiante = new Estudiante();
        System.out.println("Nuevo estudiante creado: " + nuevoEstudiante);
        model.addAttribute("estudiante", nuevoEstudiante);
        return "formulario";
    }


    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante) {
        estudianteService.guardarEstudiante(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Optional<Estudiante> estudiante = estudianteService.obtenerEstudiante(id);
        if (estudiante.isPresent()) {
            model.addAttribute("estudiante", estudiante.get());
            return "formulario";
        } else {
            return "redirect:/estudiantes";
        }
    }


    @PostMapping("/actualizar")
    public String actualizarEstudiante(@ModelAttribute Estudiante estudiante) {
        estudianteService.actualizarEstudiante(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id) { // Se cambió int a Long
        estudianteService.eliminarEstudiante(id);
        return "redirect:/estudiantes";
    }
}
