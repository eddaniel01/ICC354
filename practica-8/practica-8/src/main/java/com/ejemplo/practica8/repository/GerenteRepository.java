package com.ejemplo.practica8.repository;

import com.ejemplo.practica8.model.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Long> {

    Optional<Gerente> findByCorreo(String correo);

}
