package com.ejemplo.practica8.views;

import com.ejemplo.practica8.model.Gerente;
import com.ejemplo.practica8.service.GerenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("perfil")
@PageTitle("Mi Perfil")
@RolesAllowed("ADMIN")
public class PerfilView extends VerticalLayout {

    private final GerenteService gerenteService;
    private final Binder<Gerente> binder = new Binder<>(Gerente.class);
    private final TextField nombre = new TextField("Nombre");
    private final EmailField correo = new EmailField("Correo electrónico");
    private final Button guardar = new Button("Guardar cambios");

    private Gerente gerenteActual;

    public PerfilView(GerenteService gerenteService) {
        this.gerenteService = gerenteService;

        setSpacing(true);
        setPadding(true);
        setAlignItems(Alignment.CENTER);
        setWidth("400px");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        add(new H2("👤 Mi Perfil"), nombre, correo, guardar);

        cargarGerenteAutenticado();
        configurarFormulario();
    }

    private void cargarGerenteAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        gerenteActual = gerenteService.findAll(0, 100).stream()
                .filter(g -> g.getCorreo().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);

        if (gerenteActual != null) {
            binder.readBean(gerenteActual);
        } else {
            Notification.show("⚠️ No se encontró el gerente autenticado");
        }
    }

    private void configurarFormulario() {
        nombre.setRequiredIndicatorVisible(true);
        correo.setRequiredIndicatorVisible(true);

        binder.forField(nombre)
                .asRequired("Nombre requerido")
                .bind(Gerente::getNombre, Gerente::setNombre);

        binder.forField(correo)
                .asRequired("Correo requerido")
                .bind(Gerente::getCorreo, Gerente::setCorreo);

        guardar.addClickListener(event -> {
            if (gerenteActual != null && binder.validate().isOk()) {
                try {
                    binder.writeBean(gerenteActual);
                    gerenteService.save(gerenteActual);
                    Notification.show("Cambios guardados ✔️");
                } catch (Exception e) {
                    Notification.show("❌ Error al guardar: " + e.getMessage());
                }
            }
        });
    }
}
