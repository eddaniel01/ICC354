package org.example.practica2.controller;

import org.example.practica2.model.Estudiante;
import org.example.practica2.service.EstudianteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    private final EstudianteService service;

    public EstudianteController(EstudianteService service) {
        this.service = service;
    }

    @GetMapping
    public String listar (Model model){
        model.addAttribute("estudiantes",service.obtenerTodos());
        return "lista-estudiantes";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo (Model model){
        model.addAttribute("estudiante", new Estudiante());
        return "formulario-estudiante";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Estudiante estudiante) {
        service.agregar(estudiante);
        return "redirect:/estudiantes";
    }

    @GetMapping("/editar/{matricula}")
    public String formularioEditar(@PathVariable int matricula, Model model) {
        model.addAttribute("estudiante", service.obtenerPorMatricula(matricula));
        return "formulario-estudiante";
    }


    @PostMapping("/actualizar")
    public String actualizar(@ModelAttribute Estudiante estudiante) {
        Estudiante existente = service.obtenerPorMatricula(estudiante.getMatricula());
        if (existente != null) {
            service.actualizar(estudiante);
        }
        return "redirect:/estudiantes";
    }



    @PostMapping("/eliminar/{matricula}")
    public String eliminar(@PathVariable int matricula) {
        service.eliminar(matricula);
        return "redirect:/estudiantes";
    }

}
