package com.ejemplo.practica6.repositorio.Seguridad;

import com.ejemplo.practica6.model.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    //Cualquier metodo que necesite incluir.
    Usuario findByUsername(String username);

    boolean existsUsuarioByUsername(String username);

    Usuario findUsuarioById(int id);

}
