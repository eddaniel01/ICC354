package com.ejemplo.practica8;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.repository.GerenteRepository;
import com.ejemplo.practica8.service.CorreoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Practica8Application {

    public static void main(String[] args) {
        SpringApplication.run(Practica8Application.class, args);
    }

    @Bean
    public CommandLineRunner crearAdminPorDefecto(GerenteRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                Gerente admin = new Gerente();
                admin.setNombre("Administrador");
                admin.setCorreo("admin@correo.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRol("ADMIN");
                repository.save(admin);
                System.out.println("✅ Usuario admin@correo.com creado con contraseña admin123");
            }
        };
    }

    @Bean
    CommandLineRunner testEnvioCorreo(CorreoService correoService) {
        return args -> {
            correoService.enviarCorreo(
                    "ssbm.danielr@gmail.com", // reemplaza por tu correo personal o uno de prueba
                    "🧪 Correo de prueba",
                    "<h1>Hola 👋</h1><p>Esto es una prueba de envío de correo con SendGrid y Spring Boot</p>"
            );
        };
    }

}
