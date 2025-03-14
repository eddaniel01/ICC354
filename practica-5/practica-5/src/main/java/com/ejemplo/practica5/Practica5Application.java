package com.ejemplo.practica5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Practica5Application {
    public static void main(String[] args) {

        SpringApplication.run(Practica5Application.class, args);
    }

}
