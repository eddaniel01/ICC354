package edu.pucmm.practica7.repository;

import edu.pucmm.practica7.encapsulaciones.Solicitud;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.UUID;

public class SolicitudRepository {
    private final DynamoDbTable<Solicitud> solicitudTable;
    private final boolean awsConfigured;

    public SolicitudRepository() {
        boolean awsTempConfigured;
        DynamoDbTable<Solicitud> tempTable = null;

        try {
            // 🔹 Usamos una región fija en lugar de AWS_REGION
            Region region = Region.US_EAST_1; // Configura la región manualmente

            // 🔹 Leer el nombre de la tabla desde las variables de entorno
            String tableName = System.getenv("DYNAMODB_TABLE_NAME");
            if (tableName == null || tableName.isEmpty()) {
                throw new RuntimeException("ERROR: La variable de entorno 'DYNAMODB_TABLE_NAME' no está configurada.");
            }

            // 🔹 Crear cliente de DynamoDB
            DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                    .region(region)
                    .build();

            // 🔹 Configurar cliente mejorado para DynamoDB
            DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();

            tempTable = enhancedClient.table(tableName, TableSchema.fromBean(Solicitud.class));

            awsTempConfigured = true;
            System.out.println("Conectado a DynamoDB en la tabla: " + tableName + " en la región: " + region);
        } catch (Exception e) {
            System.out.println("AWS NO CONFIGURADO: Error al conectar con DynamoDB.");
            e.printStackTrace();
            awsTempConfigured = false;
        }

        this.solicitudTable = tempTable;
        this.awsConfigured = awsTempConfigured;
    }
    public List<Solicitud> obtenerReservasPorFechaYHora(String fecha, String hora) {
        if (!awsConfigured || solicitudTable == null) {
            System.out.println("⚠️ No se pueden obtener reservas porque DynamoDB no está configurado.");
            return List.of();
        }

        return solicitudTable.scan().items().stream()
                .filter(reserva -> reserva.getFecha().equals(fecha) && reserva.getHora().equals(hora))
                .toList();
    }

    public void guardar(Solicitud solicitud) {
        if (solicitud.getId() == null || solicitud.getId().isEmpty()) {
            solicitud.setId(UUID.randomUUID().toString()); // Genera un ID único si no lo tiene
        }
        solicitudTable.putItem(solicitud);
    }

    public List<Solicitud> listarSolicitudes() {
        if (awsConfigured && solicitudTable != null) {
            return solicitudTable.scan().items().stream().toList();
        } else {
            System.out.println(" No se pueden listar solicitudes porque DynamoDB no está configurado.");
            return List.of();
        }
    }

    public void eliminarPorId(String id) {
        if (awsConfigured && solicitudTable != null) {
            solicitudTable.deleteItem(r -> r.key(k -> k.partitionValue(id)));
        } else {
            System.out.println("No se pudo eliminar la solicitud porque DynamoDB no está configurado.");
        }
    }
}
