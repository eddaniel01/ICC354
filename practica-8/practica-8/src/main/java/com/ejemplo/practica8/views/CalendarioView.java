package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Evento;
import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.EventoService;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Route("calendario")
@PageTitle("Calendario Simplificado")
@RolesAllowed({"ADMIN", "USER"})
public class CalendarioView extends VerticalLayout {

    private final EventoService eventoService;
    private final GerenteService gerenteService;
    private Gerente gerenteAutenticado;

    private final DatePicker datePicker = new DatePicker("Selecciona una fecha");
    private final Grid<Evento> grid = new Grid<>(Evento.class, false);
    private final Button nuevoEventoBtn = new Button("➕ Nuevo evento");

    public CalendarioView(EventoService eventoService, GerenteService gerenteService) {
        this.eventoService = eventoService;
        this.gerenteService = gerenteService;

        cargarGerenteAutenticado();
        configurarUI();
        configurarEventos();
    }

    private void cargarGerenteAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();
        gerenteAutenticado = gerenteService.findByCorreo(correo);
        if (gerenteAutenticado == null) {
            Notification.show("⚠️ No se encontró el gerente autenticado");
        }
    }

    private void configurarUI() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 titulo = new H1("📅 Mi Calendario");
        datePicker.setValue(LocalDate.now());

        HorizontalLayout barraSuperior = new HorizontalLayout(datePicker, nuevoEventoBtn);
        barraSuperior.setSpacing(true);

        grid.addColumn(Evento::getTitulo).setHeader("Título");
        grid.addColumn(evento -> evento.getFechaInicio().toLocalTime()).setHeader("Inicio");
        grid.addColumn(evento -> evento.getFechaFin().toLocalTime()).setHeader("Fin");
        grid.addComponentColumn(evento -> {
            Button eliminar = new Button("Eliminar", e -> {
                eventoService.delete(evento.getId());
                actualizarGrid(datePicker.getValue());
                Notification.show("Evento eliminado ❌");
            });
            eliminar.getStyle().set("color", "red");
            return eliminar;
        }).setHeader("Acciones");

        add(titulo, barraSuperior, grid);
    }

    private void configurarEventos() {
        datePicker.addValueChangeListener(e -> actualizarGrid(e.getValue()));
        nuevoEventoBtn.addClickListener(e -> abrirDialogoNuevoEvento());
        actualizarGrid(LocalDate.now());
    }

    private void actualizarGrid(LocalDate fecha) {
        if (gerenteAutenticado != null && fecha != null) {
            List<Evento> eventos = eventoService.findByGerenteId(gerenteAutenticado.getId()).stream()
                    .filter(ev -> ev.getFechaInicio().toLocalDate().equals(fecha))
                    .toList();
            grid.setItems(eventos);
        }
    }

    private void abrirDialogoNuevoEvento() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Crear nuevo evento");

        TextField titulo = new TextField("Título");
        TextArea descripcion = new TextArea("Descripción");

        Button guardar = new Button("Guardar", e -> {
            if (!titulo.getValue().trim().isEmpty()) {
                Evento nuevo = new Evento();
                nuevo.setTitulo(titulo.getValue().trim());
                nuevo.setDescripcion(descripcion.getValue());
                nuevo.setFechaInicio(LocalDateTime.of(datePicker.getValue(), LocalTime.of(9, 0)));
                nuevo.setFechaFin(LocalDateTime.of(datePicker.getValue(), LocalTime.of(10, 0)));
                nuevo.setGerente(gerenteAutenticado);

                eventoService.save(nuevo);
                dialog.close();
                actualizarGrid(datePicker.getValue());
                Notification.show("Evento creado ✔️");
            } else {
                Notification.show("El título es obligatorio");
            }
        });

        dialog.add(new VerticalLayout(titulo, descripcion, guardar));
        dialog.open();
    }
}
