
# Manual de Despliegue CloudFormation – funds-btg

Este documento describe **paso a paso** cómo desplegar el backend **funds-btg** en AWS usando **CloudFormation**, siguiendo un enfoque realista para una **prueba técnica técnica–operativa**.

La arquitectura utiliza:
- ECS Fargate
- API Gateway (REST API, dominio por defecto HTTPS)
- ECR
- ALB
- MongoDB Atlas (externo a AWS)

---

## Arquitectura general

Flujo de alto nivel:

1. CloudFormation crea la infraestructura base (red, seguridad, repositorio).
2. Se construye y publica la imagen Docker en ECR.
3. CloudFormation despliega el backend en ECS Fargate.
4. CloudFormation expone el servicio mediante API Gateway.
5. Se valida el despliegue.

---

## Reglas importantes antes de empezar

- CloudFormation **NO construye ni sube imágenes Docker**.
- La imagen Docker **debe existir en ECR antes** de desplegar ECS.
- API Gateway **no se conecta directamente a ECS**, siempre lo hace a través de un **Application Load Balancer (ALB)**.
- El backend escucha por el **puerto 8080**.
- El endpoint `/health` está expuesto y se usa para health checks.
- La configuración está en `application.properties` (no variables de entorno).

---

## Orden obligatorio de despliegue

Este orden **no es negociable**:

1. `network.yml`
2. `security.yml`
3. `ecr.yml`
4. Build & Push de la imagen Docker
5. `ecs.yml`
6. `apigateway.yml`

---

## Paso 1: Infraestructura base (CloudFormation)

### 1.1 network.yml
Responsabilidades:
- VPC
- Subnets públicas
- Internet Gateway
- Route Tables

Resultado:
- Red lista para ECS y ALB

---

### 1.2 security.yml
Responsabilidades:
- Security Group del ALB (HTTP interno)
- Security Group de ECS (puerto 8080)
- Reglas de tráfico entre ALB y ECS

Resultado:
- Comunicación segura y controlada

---

### 1.3 ecr.yml
Responsabilidades:
- Repositorio ECR llamado `funds-btg`

Resultado:
- Repositorio listo para recibir imágenes Docker

---

## Paso 2: Build & Push de la imagen (Manual)

Este paso **NO lo hace CloudFormation**.

### 2.1 Build de la imagen
- Se construye la imagen Docker del backend.

### 2.2 Login en ECR
- Autenticación contra AWS ECR.

### 2.3 Push a ECR
- Se sube la imagen al repositorio `funds-btg`.

Resultado:
- Imagen disponible para ECS.

---

## Paso 3: Despliegue del backend (CloudFormation)

### 3.1 ecs.yml
Responsabilidades:
- ECS Cluster (Fargate)
- Task Definition
- Service
- Application Load Balancer
- Target Group
- Health Check `/health`

Configuración clave:
- Puerto contenedor: 8080
- Desired tasks: 1
- Launch type: FARGATE

Resultado:
- Backend corriendo y estable

---

## Paso 4: Exposición del servicio (CloudFormation)

### 4.1 apigateway.yml
Responsabilidades:
- API Gateway REST API
- VPC Link
- Integración con ALB
- Rutas proxy hacia el backend

Características:
- HTTPS por defecto
- Dominio estándar de API Gateway

Resultado:
- API pública accesible

---

## Paso 5: Validación final

Validaciones mínimas:
- Acceso al endpoint público de API Gateway
- Respuesta correcta del backend
- Health check exitoso
- Logs visibles en CloudWatch

---

## Endpoints del backend

```
POST   /api/v1/auth/register
POST   /api/v1/auth/login

GET    /api/v1/users/{userId}
GET    /api/v1/users/email/{email}

GET    /api/v1/funds
GET    /api/v1/funds/{id}
GET    /api/v1/funds/category/{category}

POST   /api/v1/subscriptions
POST   /api/v1/subscriptions/cancel
GET    /api/v1/subscriptions/active/{userId}
GET    /api/v1/subscriptions/history/{userId}
GET    /api/v1/subscriptions/{subscriptionId}
```

---

## Cómo defender este despliegue en entrevista

Puntos clave:
- Separación clara de responsabilidades por template.
- CloudFormation usado para infraestructura, no para build.
- ECS Fargate reduce carga operativa.
- API Gateway desacopla exposición pública del backend.
- Arquitectura simple, segura y escalable.

---

## Estado final esperado

- Infraestructura reproducible con CloudFormation.
- Backend funcional en ECS Fargate.
- API pública segura vía HTTPS.
- Solución defendible como proyecto real.

