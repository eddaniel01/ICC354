package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Evento;
import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.EventoService;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.stefan.fullcalendar.*;

import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("calendario")
@PageTitle("Calendario de Eventos")
@RolesAllowed("ADMIN")
public class CalendarioView extends VerticalLayout {

    private final EventoService eventoService;
    private final GerenteService gerenteService;
    private final FullCalendar calendar;
    private final InMemoryEntryProvider<Entry> entryProvider;
    private Gerente gerenteAutenticado;

    public CalendarioView(EventoService eventoService, GerenteService gerenteService) {
        this.eventoService = eventoService;
        this.gerenteService = gerenteService;

        this.entryProvider = new InMemoryEntryProvider<>();
        this.calendar = FullCalendarBuilder.create().withEntryProvider(entryProvider).build();

        cargarGerenteAutenticado();
        setSizeFull();
        configurarUI();
        configurarCalendario();
        add(calendar);
    }

    private void cargarGerenteAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName(); // Spring Security devuelve el "username", en este caso es el correo

        gerenteAutenticado = gerenteService.findAll(0, 100)
                .stream()
                .filter(g -> g.getCorreo().equalsIgnoreCase(correo))
                .findFirst()
                .orElse(null);

        if (gerenteAutenticado == null) {
            Notification.show("⚠️ No se pudo cargar el gerente autenticado.", 5000, Notification.Position.MIDDLE);
        }
    }

    private void configurarUI() {
        H1 titulo = new H1("📅 Calendario de Eventos");

        Button vistaMes = new Button("Mes", new Icon(VaadinIcon.CALENDAR));
        Button vistaSemana = new Button("Semana", new Icon(VaadinIcon.CALENDAR_CLOCK));
        Button vistaDia = new Button("Día", new Icon(VaadinIcon.CALENDAR_USER));

        vistaMes.addClickListener(e -> calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH));
        vistaSemana.addClickListener(e -> calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK));
        vistaDia.addClickListener(e -> calendar.changeView(CalendarViewImpl.TIME_GRID_DAY));

        vistaMes.getStyle().set("background", "#1E88E5").set("color", "white");
        vistaSemana.getStyle().set("background", "#43A047").set("color", "white");
        vistaDia.getStyle().set("background", "#FB8C00").set("color", "white");

        Button perfilBtn = new Button("Mi Perfil", new Icon(VaadinIcon.USER));
        perfilBtn.addClickListener(e -> UI.getCurrent().navigate("perfil"));
        perfilBtn.getStyle().set("background", "#6A1B9A").set("color", "white");


        HorizontalLayout botones = new HorizontalLayout(vistaMes, vistaSemana, vistaDia, perfilBtn);
        botones.setSpacing(true);
        botones.setPadding(true);

        add(titulo, botones);

    }

    private void configurarCalendario() {
        calendar.changeView(CalendarViewImpl.TIME_GRID_WEEK);
        calendar.setHeight("700px");

        if (gerenteAutenticado != null) {
            List<Evento> eventos = eventoService.findByGerenteId(gerenteAutenticado.getId());
            eventos.forEach(this::agregarEntradaDesdeEvento);
        }

        // Drag and drop
        calendar.addEntryDroppedListener(event -> {
            Entry entry = event.getEntry();
            Evento ev = new Evento();
            ev.setId(Long.parseLong(entry.getId()));
            ev.setTitulo(entry.getTitle());
            ev.setFechaInicio(entry.getStart());
            ev.setFechaFin(entry.getEnd());
            ev.setGerente(gerenteAutenticado); // Asegura asociación

            eventoService.save(ev);
            Notification.show("Evento actualizado ✔️", 3000, Notification.Position.MIDDLE);
        });

        // Crear nuevo evento
        calendar.addTimeslotsSelectedListener(event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Nuevo Evento");

            TextField titulo = new TextField("Título del evento");
            Button crear = new Button("Crear evento", click -> {
                if (!titulo.getValue().trim().isEmpty() && gerenteAutenticado != null) {
                    Evento nuevo = new Evento();
                    nuevo.setTitulo(titulo.getValue().trim());
                    nuevo.setFechaInicio(event.getStart());
                    nuevo.setFechaFin(event.getEnd());
                    nuevo.setGerente(gerenteAutenticado);

                    Evento saved = eventoService.save(nuevo);
                    agregarEntradaDesdeEvento(saved);

                    Notification.show("Evento creado ✔️", 3000, Notification.Position.MIDDLE);
                    dialog.close();
                } else {
                    Notification.show("Debe ingresar un título y tener un gerente válido", 3000, Notification.Position.MIDDLE);
                }
            });

            dialog.add(new VerticalLayout(titulo, crear));
            dialog.setDraggable(true);
            dialog.setResizable(true);
            dialog.setWidth("350px");
            dialog.open();
        });
    }

    private void agregarEntradaDesdeEvento(Evento evento) {
        Entry entry = new Entry(String.valueOf(evento.getId()));
        entry.setTitle(evento.getTitulo());
        entry.setStart(evento.getFechaInicio());
        entry.setEnd(evento.getFechaFin());

        entry.setColor("#1976D2");
        entry.setDescription("📅 Del " +
                evento.getFechaInicio().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")) +
                " al " +
                evento.getFechaFin().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")));

        entryProvider.addEntry(entry);
    }
}
