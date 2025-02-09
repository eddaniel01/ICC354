package com.ejemplo.practica2.repository;

import com.ejemplo.practica2.model.MockAPI;
import com.ejemplo.practica2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockAPIRepository extends JpaRepository<MockAPI, Long> {
    List<MockAPI> findByUsuario(Usuario usuario);
}
