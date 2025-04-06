package com.ejemplo.practica8.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CorreoService {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    public void enviarCorreo(String to, String asunto, String contenidoHtml) {
        System.out.println("📤 Intentando enviar correo a: " + to);

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", contenidoHtml);
        Mail mail = new Mail(from, asunto, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            System.out.println("✅ Correo enviado. Status: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
        } catch (IOException ex) {
            System.err.println("❌ Error al enviar correo: " + ex.getMessage());
        }
    }

}
