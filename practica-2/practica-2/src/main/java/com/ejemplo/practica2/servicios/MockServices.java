package com.ejemplo.practica2.servicios;

import com.ejemplo.practica2.model.Mock;
import com.ejemplo.practica2.model.seguridad.Usuario;
import com.ejemplo.practica2.repositorio.MockRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@EnableScheduling
public class MockServices{
    private MockRepository mockRepository;

    public MockServices(MockRepository mockRepository) {
        this.mockRepository = mockRepository;
    }

    public List<Mock> findAllByUsuario(Usuario usuario){
        return mockRepository.findAllByUsuario(usuario);
    }

    public Mock findById(int id){
        Optional<Mock> mock = mockRepository.findById(id);
        return mock.orElse(null);
    }

    public Mock crearMock(Mock mock){
        return mockRepository.save(mock);
    }

    public boolean existsByRuta(String ruta){
        return mockRepository.existsByRuta(ruta);
    }

    public void deleteById(int id){
        mockRepository.deleteById(id);
    }

    public Mock findByRuta(String ruta){return mockRepository.findByRuta(ruta);}

    @Transactional
    @Scheduled(fixedRate = 900000)
    public void deleteByFechaExpiracionLessThan(){
        mockRepository.deleteAllByFechaExpiracionLessThan(LocalDateTime.now());
    }
}

