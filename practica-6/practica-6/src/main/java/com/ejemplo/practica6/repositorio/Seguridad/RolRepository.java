package com.ejemplo.practica6.repositorio.Seguridad;

import com.ejemplo.practica6.model.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol,String> {

    //Cualquier metodo que necesite incluir.
}

