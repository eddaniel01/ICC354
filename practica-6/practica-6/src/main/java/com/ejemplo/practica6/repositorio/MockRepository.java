package com.ejemplo.practica6.repositorio;

import com.ejemplo.practica6.model.Mock;
import com.ejemplo.practica6.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MockRepository extends JpaRepository<Mock, Integer> {

    List<Mock> findAllByUsuario(Usuario usuario);

    boolean existsByRuta(String ruta);

    Mock findByRuta(String ruta);

    void deleteAllByFechaExpiracionLessThan(LocalDateTime lt);
}
