package com.ejemplo.practica8.service;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.repository.GerenteRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;

@Service
public class GerenteServiceImpl implements GerenteService {

    private final GerenteRepository repository;
    private final CorreoService correoService;

    private final PasswordEncoder passwordEncoder;

    public GerenteServiceImpl(GerenteRepository repository, CorreoService correoService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.correoService = correoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Gerente> findAll(int offset, int limit) {
        return repository.findAll(PageRequest.of(offset / limit, limit)).getContent();
    }

    @Override
    public int count() {
        return (int) repository.count();
    }

    @Override
    public void save(Gerente gerente) {
        boolean esNuevo = gerente.getId() == null;

        if (esNuevo) {
            gerente.setPassword(passwordEncoder.encode(gerente.getPassword()));
            if (gerente.getRol() == null) {
                gerente.setRol("USER"); // Valor por defecto
            }
        }

        repository.save(gerente);

        if (esNuevo) {
            String mensaje = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);">
                <h2 style="color: #1976D2;">¡Bienvenido, %s! 👋</h2>
                <p>Nos emociona que te unas a nuestra plataforma de gestión de eventos.</p>
                <p>Puedes acceder a tu calendario desde el siguiente botón:</p>
                <div style="text-align: center; margin: 30px 0;">
                    <a href="http://localhost:8080/calendario" 
                       style="background-color: #1976D2; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold;">
                       Abrir Calendario 📅
                    </a>
                </div>
                <p>Tu usuario registrado es: <strong>%s</strong></p>
                <p style="margin-top: 30px;">¡Gracias por usar nuestra aplicación!</p>
                <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                <p style="font-size: 12px; color: #888;">Este correo fue generado automáticamente. No respondas a este mensaje.</p>
            </div>
        </body>
        </html>
        """.formatted(gerente.getNombre(), gerente.getCorreo());

            correoService.enviarCorreo(
                    gerente.getCorreo(),
                    "🎉 ¡Bienvenido a la Agenda de Eventos!",
                    mensaje
            );
        }
    }

    @Override
    public void delete(Gerente gerente) {
        repository.delete(gerente);
    }
}
