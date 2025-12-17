FROM openjdk:21-jdk-slim

# Crear directorio de trabajo
WORKDIR /app

# Instalar curl para health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copiar el JAR generado
COPY target/funds-btg-1.0.0.jar app.jar

# Crear usuario no-root para ejecutar la aplicación
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Exponer el puerto
EXPOSE 8080

# Configurar JVM para optimizar memoria en contenedor
ENV JAVA_OPTS="-Xms512m -Xmx1024m \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -XX:+UseStringDeduplication"

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

