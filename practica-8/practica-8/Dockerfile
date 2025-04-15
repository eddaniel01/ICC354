FROM gradle:8.5-jdk17-alpine

# Crea y entra en la carpeta de la app
WORKDIR /app

# Copia todo al contenedor
COPY . .

# Da permisos al wrapper
RUN chmod +x ./gradlew

# Por defecto, arranca la app
CMD ["./gradlew", "bootRun"]