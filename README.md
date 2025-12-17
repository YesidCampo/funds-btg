# Proyecto Spring Boot - Gestión de Fondos

API REST para la gestión de fondos de inversión.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- MongoDB

## Requisitos

- JDK 21
- Maven 3.9+
- MongoDB local o Atlas

## Instalación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/tu-repo.git
   cd tu-repo
   
2. Configura la conexión a MongoDB en `src/main/resources/application.properties`:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/tu_base_de_datos
   
3. Construye el proyecto con Maven:
   ```bash
   mvn clean install
4. Ejecuta la aplicación:
   ```bash
    mvn spring-boot:run
   
La API estará disponible en `http://localhost:8080`.

## Endpoints
### Autenticación
- POST /api/v1/auth/register — Registrar nuevo usuario
- POST /api/v1/auth/login — Iniciar sesión (obtener token JWT)

### Usuarios
- GET /api/v1/users/{userId} — Obtener usuario por ID
- GET /api/v1/users/email/{email} — Obtener usuario por email

### Fondos
- GET /api/v1/funds — Listar todos los fondos activos
- GET /api/v1/funds/{id} — Obtener fondo por ID
- GET /api/v1/funds/category/{category} — Listar fondos por categoría (FPV o FIC)

### Suscripciones
- POST /api/v1/subscriptions — Suscribirse a un fondo
- POST /api/v1/subscriptions/cancel — Cancelar suscripción a un fondo
- GET /api/v1/subscriptions/active/{userId} — Listar suscripciones activas de un usuario
- GET /api/v1/subscriptions/history/{userId} — Historial de suscripciones de un usuario
- GET /api/v1/subscriptions/{subscriptionId} — Obtener suscripción por ID

## Pruebas 
Para ejecutar las pruebas unitarias, utiliza el siguiente comando:
```bash
mvn test

