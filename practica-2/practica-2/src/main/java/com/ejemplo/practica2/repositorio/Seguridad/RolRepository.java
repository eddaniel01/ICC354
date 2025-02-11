package com.ejemplo.practica2.repositorio.Seguridad;

import com.ejemplo.practica2.model.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol,String> {

    //Cualquier metodo que necesite incluir.
}

