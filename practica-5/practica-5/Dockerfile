# Usamos la imagen oficial de OpenJDK
FROM openjdk:17-jdk-slim

# Configurar el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el archivo JAR generado
COPY build/libs/practica-5-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto donde corre Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
