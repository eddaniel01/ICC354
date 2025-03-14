# Etapa 1: Construcción
FROM gradle:8.3-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon

# Etapa 2: Ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080

# Variables de entorno para la base de datos
ENV DB_HOST=db
ENV DB_PORT=3306
ENV DB_NAME=testdb
ENV DB_USER=testuser
ENV DB_PASSWORD=testpass

ENTRYPOINT ["java", "-jar", "app.jar"]
