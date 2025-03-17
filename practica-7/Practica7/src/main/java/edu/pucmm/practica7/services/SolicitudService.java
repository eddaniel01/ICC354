package edu.pucmm.practica7.services;

import edu.pucmm.practica7.encapsulaciones.Solicitud;
import edu.pucmm.practica7.repository.SolicitudRepository;

import java.util.List;
import java.util.stream.Collectors;

public class SolicitudService {
    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public void registrarSolicitud(Solicitud solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("❌ Error: La solicitud no puede ser nula.");
        }

        // Validar que todos los campos estén llenos
        if (solicitud.getMatricula() == null || solicitud.getMatricula().trim().isEmpty() ||
                solicitud.getNombre() == null || solicitud.getNombre().trim().isEmpty() ||
                solicitud.getCorreo() == null || solicitud.getCorreo().trim().isEmpty() ||
                solicitud.getLaboratorio() == null || solicitud.getLaboratorio().trim().isEmpty() ||
                solicitud.getFecha() == null || solicitud.getFecha().trim().isEmpty() ||
                solicitud.getHora() == null || solicitud.getHora().trim().isEmpty()) {

            throw new IllegalArgumentException("Error: Todos los campos son obligatorios.");
        }

        // Nueva validación: Verificar que la hora esté entre 08:00 y 22:00
        if (!validarHorario(solicitud.getHora())) {
            throw new IllegalArgumentException("Error: La hora debe estar entre 08:00 AM y 10:00 PM en intervalos de 1 hora.");
        }

        // Verificar si hay más de 7 reservas en la misma fecha y hora
        List<Solicitud> reservasEnMismaHora = solicitudRepository.obtenerReservasPorFechaYHora(
                solicitud.getFecha(), solicitud.getHora()
        );

        if (reservasEnMismaHora.size() >= 7) {
            throw new IllegalArgumentException("No se puede registrar la reserva. Ya hay 7 reservas en esta hora.");
        }

        // Guardar en DynamoDB
        solicitudRepository.guardar(solicitud);
    }

    private boolean validarHorario(String hora) {
        try {
            int horaInt = Integer.parseInt(hora.split(":")[0]);
            // Permitir solo horarios entre 08:00 y 22:00
            return horaInt >= 8 && horaInt <= 22;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Solicitud> obtenerSolicitudesActivas() {
        return solicitudRepository.listarSolicitudes();
    }

    public void eliminarSolicitud(String id) {
        solicitudRepository.eliminarPorId(id);
    }
}