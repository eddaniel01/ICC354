# Etapa 1: Construcción de la aplicación
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiamos los archivos de Gradle y resolvemos dependencias
COPY gradle gradle
COPY build.gradle settings.gradle gradlew gradlew.bat ./
RUN chmod +x gradlew
RUN ./gradlew build || return 0

# Copiamos el código fuente y construimos la aplicación
COPY src src
RUN ./gradlew bootJar

# Etapa 2: Imagen final con el JAR optimizado
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Puerto configurable mediante variable de entorno (por defecto 8080)
ENV SERVER_PORT=8080

# Copiamos solo el JAR generado en la etapa anterior
COPY --from=builder /app/build/libs/app.jar app.jar

# Configuramos un volumen para almacenar logs
VOLUME /app/logs

# Ejecutamos la aplicación con el puerto definido por la variable de entorno
CMD ["sh", "-c", "java -jar -Dserver.port=${SERVER_PORT} app.jar"]
