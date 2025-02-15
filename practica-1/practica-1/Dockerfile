# Etapa 1: Construcción
# Asegura compatibilidad con JDK 21 y Gradle 8.3
FROM gradle:8.3-jdk21 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .

RUN gradle build --no-daemon

# Etapa 2: Ejecución con JRE
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

# Configuración del puerto con variable de entorno
ENV SERVER_PORT=8080
EXPOSE $SERVER_PORT

# Definir volumen para los logs
VOLUME /logs
ENV LOG_PATH=/logs/app.log

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-Dlogging.file.path=${LOG_PATH}", "-jar", "app.jar"]
