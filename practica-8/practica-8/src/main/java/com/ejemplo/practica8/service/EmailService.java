package com.ejemplo.practica8.service;

public interface EmailService {
    void enviarCorreo(String destinatario, String asunto, String mensajeHtml);
}
