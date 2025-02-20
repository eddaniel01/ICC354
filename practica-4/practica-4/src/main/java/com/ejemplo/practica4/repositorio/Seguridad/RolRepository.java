package com.ejemplo.practica4.repositorio.Seguridad;

import com.ejemplo.practica4.model.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol,String> {

    //Cualquier metodo que necesite incluir.
}

