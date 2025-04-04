package com.ejemplo.practica8.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView() {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            UI.getCurrent().navigate("gerentes");
        } else {
            UI.getCurrent().navigate("login");
        }
    }
}

