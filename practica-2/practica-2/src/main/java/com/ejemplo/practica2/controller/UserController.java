package com.ejemplo.practica2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/secure-data")
    public String getSecureData() {
        return "¡Acceso permitido! Datos protegidos.";
    }

}
