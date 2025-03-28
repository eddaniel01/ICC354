package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("gerentes")
@PageTitle("Gestión de Gerentes")
@RolesAllowed("ADMIN") // Solo ADMIN puede acceder
public class GerenteView extends VerticalLayout {

    private final GerenteService service;
    private final Grid<Gerente> grid = new Grid<>(Gerente.class, false);
    private final TextField nombre = new TextField("Nombre");
    private final EmailField correo = new EmailField("Correo electrónico");
    private final Button guardar = new Button("Guardar");
    private final Button eliminar = new Button("Eliminar");
    private final Binder<Gerente> binder = new Binder<>(Gerente.class);

    private Gerente gerenteSeleccionado;

    public GerenteView(GerenteService service) {
        this.service = service;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        configurarGrid();
        configurarFormulario();

        add(grid, crearFormulario());
    }

    private void configurarGrid() {
        grid.addColumn(Gerente::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Gerente::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Gerente::getCorreo).setHeader("Correo").setAutoWidth(true);
        grid.setHeight("300px");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setDataProvider(new CallbackDataProvider<>(
                query -> service.findAll(query.getOffset(), query.getLimit()).stream(),
                query -> service.count()
        ));

        grid.asSingleSelect().addValueChangeListener(event -> {
            gerenteSeleccionado = event.getValue();
            binder.readBean(gerenteSeleccionado);
        });
    }

    private void configurarFormulario() {
        binder.bindInstanceFields(this);
        limpiarFormulario();
    }

    private FormLayout crearFormulario() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(nombre, correo);

        binder.bindInstanceFields(this);

        guardar.addClickListener(e -> {
            if (gerenteSeleccionado == null) {
                gerenteSeleccionado = new Gerente();
            }
            try {
                binder.writeBean(gerenteSeleccionado);
                service.save(gerenteSeleccionado);
                grid.getDataProvider().refreshAll();
                Notification.show("Guardado exitosamente");
                limpiarFormulario();
            } catch (Exception ex) {
                Notification.show("Error al guardar: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        eliminar.addClickListener(e -> {
            if (gerenteSeleccionado != null) {
                service.delete(gerenteSeleccionado);
                grid.getDataProvider().refreshAll();
                Notification.show("Eliminado");
                limpiarFormulario();
            }
        });

        HorizontalLayout botones = new HorizontalLayout(guardar, eliminar);
        VerticalLayout layout = new VerticalLayout(formLayout, botones);
        layout.setAlignItems(Alignment.START);
        return new FormLayout(layout);
    }

    private void limpiarFormulario() {
        gerenteSeleccionado = null;
        binder.readBean(new Gerente());
    }
}
