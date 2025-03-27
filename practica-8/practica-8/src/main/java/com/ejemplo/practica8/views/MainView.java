package com.ejemplo.practica8.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Button button = new Button("Haz clic aquí", event ->
                Notification.show("¡Hola desde Vaadin!")
        );
        add(button);
    }
}
