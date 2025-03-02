# Usamos la imagen de Java 21 para correr el backend
FROM eclipse-temurin:21-jdk

# Establecer el directorio de trabajo en el contenedor
WORKDIR /app

# Copiar el código fuente del backend al contenedor
COPY . .

# Construir el proyecto con Gradle
RUN ./gradlew bootJar

# Exponer el puerto de la aplicación
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "build/libs/practica-5-0.0.1-SNAPSHOT.jar"]

