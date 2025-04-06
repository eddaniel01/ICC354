package com.ejemplo.practica8.service;

import com.ejemplo.practica8.model.Evento;
import com.ejemplo.practica8.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class EventoServiceImpl implements EventoService {

    private final EventoRepository repository;
    private final CorreoService correoService;

    public EventoServiceImpl(EventoRepository repository, CorreoService correoService) {
        this.repository = repository;
        this.correoService = correoService;
    }

    @Override
    public List<Evento> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Evento> findByGerenteId(Long gerenteId) {
        return repository.findByGerenteId(gerenteId);
    }

    @Override
    public Evento save(Evento evento) {
        Evento saved = repository.save(evento);

        // Si el evento no tiene gerente asignado, no enviamos correo
        if (saved.getGerente() == null || saved.getGerente().getCorreo() == null) {
            System.out.println("❌ Evento guardado pero sin gerente asignado. No se envió correo.");
            return saved;
        }

        // Formateo de fecha bonita en español
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM 'a las' HH:mm", new Locale("es", "ES"));

        String mensajeHtml = """
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px;">
            <div style="background-color: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                <h2 style="color: #1976D2;">¡Tienes un nuevo evento programado!</h2>
                <p>Hola <strong>%s</strong>,</p>
                <p>Se ha agendado un nuevo evento en tu calendario:</p>
                <ul>
                    <li><strong>Título:</strong> %s</li>
                    <li><strong>Descripción:</strong> %s</li>
                    <li><strong>Inicio:</strong> %s</li>
                    <li><strong>Fin:</strong> %s</li>
                </ul>
                <p>Puedes consultar el evento en tu calendario desde <a href="http://localhost:8080/calendario">aquí</a>.</p>
                <p style="color: #777; font-size: 12px;">Este correo se generó automáticamente. No responder.</p>
            </div>
        </body>
        </html>
        """.formatted(
                saved.getGerente().getNombre(),
                saved.getTitulo(),
                saved.getDescripcion() != null ? saved.getDescripcion() : "(Sin descripción)",
                saved.getFechaInicio().format(formatter),
                saved.getFechaFin().format(formatter)
        );

        // Envío
        correoService.enviarCorreo(
                saved.getGerente().getCorreo(),
                "📌 Nuevo evento en tu calendario",
                mensajeHtml
        );

        return saved;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
