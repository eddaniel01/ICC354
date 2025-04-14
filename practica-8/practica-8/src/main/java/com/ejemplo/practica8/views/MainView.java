package com.ejemplo.practica8.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
@PageTitle("Inicio")
@PermitAll // ← Acepta a usuarios autenticados y anónimos
public class MainView extends VerticalLayout implements AfterNavigationObserver {

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        VaadinSession.getCurrent().getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");

        if (auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken)) {

            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                UI.getCurrent().navigate("gerentes");
            } else {
                UI.getCurrent().navigate("calendario");
            }

        } else {
            UI.getCurrent().navigate("login");
        }
    }
}
