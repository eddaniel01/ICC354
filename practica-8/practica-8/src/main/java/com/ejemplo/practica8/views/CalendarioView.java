package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Evento;
import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.EventoService;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.stefan.fullcalendar.*;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import com.vaadin.flow.component.combobox.ComboBox;
import java.time.Month;
import java.time.YearMonth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Route("calendario")
@PageTitle("Calendario Interactivo")
@RolesAllowed({"ADMIN", "USER"})
public class CalendarioView extends VerticalLayout {

    private final EventoService eventoService;
    private final GerenteService gerenteService;
    private Gerente gerenteAutenticado;

    private FullCalendar calendar;
    private InMemoryEntryProvider<Entry> provider;

    public CalendarioView(EventoService eventoService, GerenteService gerenteService) {
        this.eventoService = eventoService;
        this.gerenteService = gerenteService;

        cargarGerenteAutenticado();
        configurarCalendario();
        cargarEventosEnCalendario();
    }

    private void cargarGerenteAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName();
        gerenteAutenticado = gerenteService.findByCorreo(correo);
    }

    private void configurarCalendario() {
        calendar = FullCalendarBuilder.create().withEntryLimit(1000).build();
        calendar.setHeight("800px");
        calendar.setWidthFull();

        calendar.setLocale(Locale.forLanguageTag("es"));

        provider = (InMemoryEntryProvider<Entry>) calendar.getEntryProvider();

        calendar.addTimeslotClickedListener(event -> {
            LocalDateTime fechaInicio = event.getDateTime();
            mostrarDialogoCrearEvento(fechaInicio);
        });

        calendar.addEntryClickedListener(event -> {
            Entry entrada = event.getEntry();
            eventoService.delete(Long.parseLong(entrada.getId()));
            provider.removeEntry(entrada);
            Notification.show("Evento eliminado");
        });


        H1 titulo = new H1("📅 Calendario de Eventos");
        titulo.getStyle()
                .set("margin", "20px 0")
                .set("font-size", "28px")
                .set("color", "#1976D2");

        ComboBox<Month> selectorMes = new ComboBox<>("Mes");
        selectorMes.setItems(Month.values());
        selectorMes.setItemLabelGenerator(month -> month.getDisplayName(java.time.format.TextStyle.FULL, new Locale("es")));
        selectorMes.setValue(LocalDateTime.now().getMonth());

        selectorMes.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                LocalDateTime nuevaFecha = YearMonth.of(LocalDateTime.now().getYear(), e.getValue()).atDay(1).atStartOfDay();
                calendar.gotoDate(nuevaFecha.toLocalDate());
            }
        });

        add(titulo, selectorMes, calendar);
    }


    private void abrirFormularioCrearEvento(LocalDateTime inicio, LocalDateTime fin) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nuevo Evento");

        TextField titulo = new TextField("Título");
        TextArea descripcion = new TextArea("Descripción");

        Button guardar = new Button("Crear Evento", e -> {
            if (!titulo.getValue().trim().isEmpty()) {
                Evento nuevo = new Evento();
                nuevo.setTitulo(titulo.getValue());
                nuevo.setDescripcion(descripcion.getValue());
                nuevo.setFechaInicio(inicio);
                nuevo.setFechaFin(fin);
                nuevo.setGerente(gerenteAutenticado);

                Evento guardado = eventoService.save(nuevo);

                Entry entrada = new Entry(String.valueOf(guardado.getId()));
                entrada.setTitle(guardado.getTitulo());
                entrada.setStart(guardado.getFechaInicio());
                entrada.setEnd(guardado.getFechaFin());
                entrada.setColor("blue");

                provider.addEntries(List.of(entrada));
                provider.refreshAll();
                dialog.close();
                Notification.show("Evento creado ✔");
            } else {
                Notification.show("El título es obligatorio");
            }
        });

        dialog.add(new VerticalLayout(titulo, descripcion, guardar));
        dialog.open();
    }

    private void cargarEventosEnCalendario() {
        if (gerenteAutenticado != null) {
            List<Evento> eventos = eventoService.findByGerenteId(gerenteAutenticado.getId());
            for (Evento ev : eventos) {
                Entry entrada = new Entry(String.valueOf(ev.getId()));
                entrada.setTitle(ev.getTitulo());
                entrada.setStart(ev.getFechaInicio());
                entrada.setEnd(ev.getFechaFin());
                entrada.setColor("blue");
                provider.addEntries(List.of(entrada));
            }
        }
    }

    private void mostrarDialogoCrearEvento(LocalDateTime fechaInicio) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nuevo Evento");

        TextField titulo = new TextField("Título");
        TextArea descripcion = new TextArea("Descripción");
        DateTimePicker fechaInicioPicker = new DateTimePicker("Fecha y hora de inicio");
        fechaInicioPicker.setValue(fechaInicio);
        DateTimePicker fechaFinPicker = new DateTimePicker("Fecha y hora de fin");
        fechaFinPicker.setValue(fechaInicio.plusHours(1));

        Button guardar = new Button("Guardar", e -> {
            if (titulo.isEmpty()) {
                Notification.show("Debes ingresar un título");
                return;
            }

            Evento nuevo = new Evento();
            nuevo.setTitulo(titulo.getValue());
            nuevo.setDescripcion(descripcion.getValue());
            nuevo.setFechaInicio(fechaInicioPicker.getValue());
            nuevo.setFechaFin(fechaFinPicker.getValue());
            nuevo.setGerente(gerenteAutenticado);

            Evento guardado = eventoService.save(nuevo);

            Entry entrada = new Entry(String.valueOf(guardado.getId()));
            entrada.setTitle(titulo.getValue() + " - " + descripcion.getValue());
            entrada.setStart(guardado.getFechaInicio());
            entrada.setEnd(guardado.getFechaFin());
            entrada.setColor("blue");

            provider.addEntries(List.of(entrada));
            provider.refreshAll();
            dialog.close();
            Notification.show("Evento creado");
        });

        VerticalLayout layout = new VerticalLayout(titulo, descripcion, fechaInicioPicker, fechaFinPicker, guardar);
        dialog.add(layout);
        dialog.open();
    }

}
