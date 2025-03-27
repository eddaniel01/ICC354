package edu.pucmm.practica7.funciones;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import edu.pucmm.practica7.encapsulaciones.Solicitud;
import edu.pucmm.practica7.services.SolicitudService;
import edu.pucmm.practica7.repository.SolicitudRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LambdaHandler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

    private final SolicitudService solicitudService;
    private final Gson gson = new Gson();

    public LambdaHandler() {
        SolicitudRepository solicitudRepository = new SolicitudRepository();
        this.solicitudService = new SolicitudService(solicitudRepository);
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent request, Context awsContext) {
        awsContext.getLogger().log("Recibiendo petición: " + gson.toJson(request));

        String metodoHttp = request.getRequestContext().getHttp().getMethod();
        String salida = "";

        try {
            switch (metodoHttp) {
                case "GET":
                    salida = gson.toJson(solicitudService.obtenerSolicitudesActivas());
                    break;

                case "POST":
                    if (request.getBody() == null || request.getBody().isEmpty()) {
                        return crearRespuesta(400, "{\"error\": \"El cuerpo de la solicitud no puede estar vacío.\"}");
                    }

                    String jsonString = request.getBody();
                    Solicitud solicitud = gson.fromJson(jsonString, Solicitud.class);

                    if (solicitud.getMatricula() == null || solicitud.getNombre() == null ||
                            solicitud.getCorreo() == null || solicitud.getLaboratorio() == null ||
                            solicitud.getFecha() == null || solicitud.getHora() == null) {
                        return crearRespuesta(400, "{\"error\": \"Todos los campos son obligatorios.\"}");
                    }

                    if (solicitud.getId() == null || solicitud.getId().isEmpty()) {
                        solicitud.setId(UUID.randomUUID().toString());
                    }

                    solicitudService.registrarSolicitud(solicitud);
                    salida = gson.toJson(solicitud);
                    break;

                case "DELETE":
                    if (request.getBody() == null || request.getBody().isEmpty()) {
                        return crearRespuesta(400, "{\"error\": \"El cuerpo de la solicitud no puede estar vacío.\"}");
                    }

                    jsonString = request.getBody();
                    Solicitud solicitudEliminar = gson.fromJson(jsonString, Solicitud.class);

                    if (solicitudEliminar.getId() == null || solicitudEliminar.getId().isEmpty()) {
                        return crearRespuesta(400, "{\"error\": \"ID de solicitud inválido.\"}");
                    }

                    solicitudService.eliminarSolicitud(solicitudEliminar.getId());
                    salida = "{\"mensaje\": \"Solicitud eliminada correctamente.\"}";
                    break;

                default:
                    return crearRespuesta(400, "{\"error\": \"Método no soportado.\"}");
            }
        } catch (JsonSyntaxException e) {
            awsContext.getLogger().log(" Error de JSON: " + e.getMessage());
            return crearRespuesta(400, "{\"error\": \"Formato de JSON inválido.\"}");
        } catch (Exception e) {
            awsContext.getLogger().log(" Error interno: " + e.getMessage());
            return crearRespuesta(500, "{\"error\": \"Error interno: " + e.getMessage() + "\"}");
        }

        return crearRespuesta(200, salida);
    }


    //  Función para incluir CORS en cada respuesta de la Lambda

    private APIGatewayV2HTTPResponse crearRespuesta(int statusCode, String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");  // Permitir peticiones desde cualquier origen
        headers.put("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        headers.put("Access-Control-Allow-Headers", "Content-Type, Authorization");

        return APIGatewayV2HTTPResponse.builder()
                .withStatusCode(statusCode)
                .withHeaders(headers)
                .withBody(body)
                .build();
    }
} 