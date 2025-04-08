package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@Route("gerentes")
@PageTitle("Gestión de Gerentes")
@RolesAllowed("ADMIN")
public class GerenteView extends VerticalLayout {

    private final GerenteService service;
    private final Grid<Gerente> grid = new Grid<>(Gerente.class, false);
    private final Button nuevoBtn = new Button("Nuevo gerente");
    private final Button editarBtn = new Button("Editar seleccionado");
    private final Button eliminarBtn = new Button("Eliminar seleccionado");

    private final Dialog dialog = new Dialog();
    private final TextField nombre = new TextField("Nombre");
    private final EmailField correo = new EmailField("Correo electrónico");

    private final PasswordField password = new PasswordField("Contraseña");
    private final Button guardarBtn = new Button("Guardar");

    private final Binder<Gerente> binder = new Binder<>(Gerente.class);
    private Gerente gerenteSeleccionado;

    public GerenteView(GerenteService service) {
        this.service = service;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        configurarGrid();
        configurarFormulario();
        configurarDialogo();

        Button perfilBtn = new Button("Mi Perfil", e -> UI.getCurrent().navigate("perfil"));
        perfilBtn.getStyle().set("background", "#6A1B9A").set("color", "white");

        HorizontalLayout topBar = new HorizontalLayout(nuevoBtn, editarBtn, eliminarBtn, perfilBtn);
        editarBtn.setEnabled(false);
        eliminarBtn.setEnabled(false);

        nuevoBtn.addClickListener(e -> {
            gerenteSeleccionado = new Gerente();
            abrirDialogo("Nuevo Gerente");
        });

        editarBtn.addClickListener(e -> {
            if (gerenteSeleccionado != null) {
                abrirDialogo("Editar Gerente");
            }
        });

        eliminarBtn.addClickListener(e -> {
            if (gerenteSeleccionado != null) {
                service.delete(gerenteSeleccionado);
                Notification.show("Gerente eliminado ❌");
                grid.getDataProvider().refreshAll();
                gerenteSeleccionado = null;
                editarBtn.setEnabled(false);
                eliminarBtn.setEnabled(false);
            }
        });

        add(topBar, grid);
    }

    private void configurarGrid() {
        grid.addColumn(Gerente::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Gerente::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Gerente::getCorreo).setHeader("Correo").setAutoWidth(true);
        grid.setHeight("450px");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.setDataProvider(new CallbackDataProvider<>(
                query -> service.findAll(query.getOffset(), query.getLimit()).stream(),
                query -> service.count()
        ));

        grid.asSingleSelect().addValueChangeListener(event -> {
            gerenteSeleccionado = event.getValue();
            boolean seleccionado = gerenteSeleccionado != null;
            editarBtn.setEnabled(seleccionado);
            eliminarBtn.setEnabled(seleccionado);
        });
    }

    private void configurarFormulario() {
        nombre.setRequiredIndicatorVisible(true);
        correo.setRequiredIndicatorVisible(true);

        binder.forField(nombre)
                .asRequired("El nombre es obligatorio")
                .withValidator(n -> n.length() >= 2, "Debe tener al menos 2 caracteres")
                .bind(Gerente::getNombre, Gerente::setNombre);

        binder.forField(correo)
                .asRequired("El correo es obligatorio")
                .withValidator(email -> email.contains("@") && email.contains("."), "Correo inválido")
                .bind(Gerente::getCorreo, Gerente::setCorreo);

        binder.forField(password)
                .asRequired("La contraseña es obligatoria")
                .bind(Gerente::getPassword, Gerente::setPassword);

        binder.addStatusChangeListener(e -> guardarBtn.setEnabled(binder.isValid()));
        guardarBtn.setEnabled(false);
    }

    private void configurarDialogo() {
        FormLayout formLayout = new FormLayout(nombre, correo, password);
        HorizontalLayout botones = new HorizontalLayout(guardarBtn);
        VerticalLayout contenido = new VerticalLayout(formLayout, botones);
        contenido.setSpacing(true);
        contenido.setAlignItems(Alignment.START);

        guardarBtn.addClickListener(e -> {
            if (binder.validate().isOk()) {
                try {
                    binder.writeBean(gerenteSeleccionado);
                    service.save(gerenteSeleccionado);
                    Notification.show("Guardado exitosamente ✔️");
                    grid.getDataProvider().refreshAll();
                    dialog.close();
                } catch (Exception ex) {
                    Notification.show("Error: " + ex.getMessage());
                }
            }
        });

        dialog.setDraggable(true);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setWidth("400px");
        dialog.add(contenido);
    }

    private void abrirDialogo(String titulo) {
        dialog.setHeaderTitle(titulo);
        binder.readBean(gerenteSeleccionado);
        dialog.open();
    }
}
