# CentroPlus Connect — Documentación de la API

## Información general

| Campo | Valor |
|---|---|
| Versión | `0.0.1-SNAPSHOT` |
| Base URL | `http://localhost:8080/api/v1` |
| Formato | JSON |
| Autenticación | JWT Bearer Token |
| Documentación interactiva | `http://localhost:8080/swagger-ui.html` |

### Tecnologías

- **Java 17** · Spring Boot 3.2.5
- **Spring Security** + JWT (jjwt 0.11.5)
- **Spring Data JPA** · Base de datos H2 (en memoria)
- **Arquitectura hexagonal** (puertos y adaptadores)
- **MapStruct** para mapeo de objetos · **Lombok** para reducción de boilerplate
- **SpringDoc OpenAPI 2.3.0** para documentación Swagger

---

## Autenticación

Todos los endpoints (excepto `/auth/login`) requieren un token JWT en la cabecera de la petición.

```
Authorization: Bearer <token>
```

### POST `/api/v1/auth/login`

Inicia sesión con DNI y contraseña y devuelve un token JWT.

**Request body:**
```json
{
  "dni": "12345678A",
  "password": "contraseña"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipoUsuario": "ALUMNO",
  "nombre": "Juan García"
}
```

**Response `401 Unauthorized`:** Credenciales incorrectas.

---

## Usuarios

Base path: `/api/v1/usuarios`

### Tipos de usuario

| Valor | Descripción |
|---|---|
| `ALUMNO` | Estudiante del centro |
| `SOCIO` | Socio del centro |
| `AMBOS` | Alumno y socio a la vez |

### Endpoints

#### `GET /api/v1/usuarios`
Lista todos los usuarios registrados.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Juan García",
    "dni": "12345678A",
    "email": "juan@ejemplo.com",
    "tipoUsuario": "ALUMNO"
  }
]
```

---

#### `GET /api/v1/usuarios/{id}`
Devuelve un usuario por su ID.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `id` | `int` (path) | ID del usuario |

---

#### `GET /api/v1/usuarios/dni/{dni}`
Busca un usuario por su DNI.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `dni` | `String` (path) | DNI del usuario |

---

#### `GET /api/v1/usuarios/email/{email}`
Busca un usuario por su email.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `email` | `String` (path) | Email del usuario |

---

#### `GET /api/v1/usuarios/tipo/{tipo}`
Lista los usuarios filtrados por tipo.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `tipo` | `String` (path) | `ALUMNO`, `SOCIO`, `AMBOS` |

---

#### `GET /api/v1/usuarios/buscar?nombre={nombre}`
Busca usuarios por nombre (búsqueda parcial, no sensible a mayúsculas).

| Parámetro | Tipo | Descripción |
|---|---|---|
| `nombre` | `String` (query) | Fragmento del nombre a buscar |

---

#### `POST /api/v1/usuarios?password={password}`
Crea un nuevo usuario.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `password` | `String` (query) | Contraseña en texto plano (se cifra con BCrypt) |

**Request body:**
```json
{
  "nombre": "Juan García",
  "dni": "12345678A",
  "email": "juan@ejemplo.com",
  "tipoUsuario": "ALUMNO"
}
```

**Response `200 OK`:** El usuario creado.

---

#### `PUT /api/v1/usuarios/{id}`
Actualiza los datos de un usuario existente.

**Request body:** igual que `POST`.

---

#### `DELETE /api/v1/usuarios/{id}`
Elimina un usuario por su ID.

**Response `204 No Content`**

---

## Actividades

Base path: `/api/v1/actividades`

### Tipos de actividad

| Valor | Descripción |
|---|---|
| `DEPORTIVA` | Actividad de tipo deportivo |
| `ACADEMICA` | Actividad de tipo académico |

### Endpoints

#### `GET /api/v1/actividades`
Lista todas las actividades disponibles.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Yoga",
    "tipo": "DEPORTIVA",
    "precio": 15.0,
    "plazasDisponibles": 10
  }
]
```

---

#### `GET /api/v1/actividades/{id}`
Devuelve una actividad por su ID.

---

#### `GET /api/v1/actividades/tipo/{tipo}`
Filtra actividades por tipo.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `tipo` | `String` (path) | `DEPORTIVA`, `ACADEMICA` |

---

#### `GET /api/v1/actividades/buscar?nombre={nombre}`
Búsqueda parcial de actividades por nombre.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `nombre` | `String` (query) | Fragmento del nombre |

---

#### `GET /api/v1/actividades/precio?max={max}`
Lista actividades con precio igual o inferior al máximo indicado.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `max` | `double` (query) | Precio máximo |

---

#### `POST /api/v1/actividades`
Crea una nueva actividad.

**Request body:**
```json
{
  "nombre": "Yoga",
  "tipo": "DEPORTIVA",
  "precio": 15.0,
  "plazasDisponibles": 10
}
```

---

#### `PUT /api/v1/actividades/{id}`
Actualiza una actividad existente.

**Request body:** igual que `POST`.

---

#### `DELETE /api/v1/actividades/{id}`
Elimina una actividad.

**Response `204 No Content`**

---

#### `PATCH /api/v1/actividades/{id}/reservar-plaza`
Decrementa en uno las plazas disponibles de la actividad.

**Response `200 OK`:**
```json
true
```

Devuelve `false` si no hay plazas disponibles.

---

#### `PATCH /api/v1/actividades/{id}/cancelar-plaza`
Incrementa en uno las plazas disponibles de la actividad.

**Response `200 OK`:**
```json
true
```

---

## Reservas

Base path: `/api/v1/reservas`

### Estados de reserva

| Valor | Descripción |
|---|---|
| `ACTIVA` | Reserva vigente |
| `CANCELADA` | Reserva cancelada |
| `COMPLETADA` | Reserva completada |

### Endpoints

#### `GET /api/v1/reservas`
Lista todas las reservas.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 1,
    "idActividad": 2,
    "estado": "ACTIVA"
  }
]
```

---

#### `GET /api/v1/reservas/{id}`
Devuelve una reserva por su ID.

---

#### `GET /api/v1/reservas/usuario/{idUsuario}`
Lista todas las reservas de un usuario.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idUsuario` | `int` (path) | ID del usuario |

---

#### `GET /api/v1/reservas/actividad/{idActividad}`
Lista todas las reservas de una actividad.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idActividad` | `int` (path) | ID de la actividad |

---

#### `GET /api/v1/reservas/estado/{estado}`
Filtra reservas por estado.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `estado` | `String` (path) | `ACTIVA`, `CANCELADA`, `COMPLETADA` |

---

#### `POST /api/v1/reservas`
Crea una nueva reserva.

**Request body:**
```json
{
  "idUsuario": 1,
  "idActividad": 2,
  "estado": "ACTIVA"
}
```

---

#### `PUT /api/v1/reservas/{id}`
Actualiza una reserva existente.

**Request body:** igual que `POST`.

---

#### `DELETE /api/v1/reservas/{id}`
Elimina una reserva.

**Response `204 No Content`**

---

#### `PATCH /api/v1/reservas/{id}/estado?estado={estado}`
Cambia el estado de una reserva.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `estado` | `String` (query) | `ACTIVA`, `CANCELADA`, `COMPLETADA` |

**Response `200 OK`:** `true` si el cambio fue exitoso.

---

#### `PATCH /api/v1/reservas/{id}/cancelar?idUsuario={idUsuario}`
Cancela la reserva de un usuario concreto.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idUsuario` | `int` (query) | ID del usuario propietario de la reserva |

**Response `200 OK`:** `true` si la cancelación fue exitosa.

---

## Incidencias

Base path: `/api/v1/incidencias`

### Estados de incidencia

| Valor | Descripción |
|---|---|
| `ABIERTA` | Incidencia recién registrada |
| `EN_PROCESO` | Incidencia en gestión |
| `CERRADA` | Incidencia resuelta |

### Endpoints

#### `GET /api/v1/incidencias`
Lista todas las incidencias.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 1,
    "asunto": "Problema con el acceso",
    "descripcion": "No puedo entrar al área de socios",
    "estado": "ABIERTA"
  }
]
```

---

#### `GET /api/v1/incidencias/{id}`
Devuelve una incidencia por su ID.

---

#### `GET /api/v1/incidencias/usuario/{idUsuario}`
Lista todas las incidencias de un usuario.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `idUsuario` | `int` (path) | ID del usuario |

---

#### `GET /api/v1/incidencias/estado/{estado}`
Filtra incidencias por estado.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `estado` | `String` (path) | `ABIERTA`, `EN_PROCESO`, `CERRADA` |

---

#### `GET /api/v1/incidencias/buscar?asunto={asunto}`
Búsqueda parcial de incidencias por asunto.

| Parámetro | Tipo | Descripción |
|---|---|---|
| `asunto` | `String` (query) | Fragmento del asunto |

---

#### `POST /api/v1/incidencias`
Crea una nueva incidencia.

**Request body:**
```json
{
  "idUsuario": 1,
  "asunto": "Problema con el acceso",
  "descripcion": "No puedo entrar al área de socios",
  "estado": "ABIERTA"
}
```

---

#### `PUT /api/v1/incidencias/{id}`
Actualiza una incidencia existente.

**Request body:** igual que `POST`.

---

#### `DELETE /api/v1/incidencias/{id}`
Elimina una incidencia.

**Response `204 No Content`**

---

#### `PATCH /api/v1/incidencias/{id}/estado?estado={estado}`
Cambia el estado de una incidencia.

| Parámetro | Tipo | Valores posibles |
|---|---|---|
| `estado` | `String` (query) | `ABIERTA`, `EN_PROCESO`, `CERRADA` |

**Response `200 OK`:** `true` si el cambio fue exitoso.

---

## Códigos de respuesta HTTP

| Código | Descripción |
|---|---|
| `200 OK` | Operación exitosa |
| `204 No Content` | Eliminación exitosa |
| `401 Unauthorized` | Token no válido o ausente |
| `404 Not Found` | Recurso no encontrado |
| `500 Internal Server Error` | Error inesperado del servidor |

---

## Arquitectura del proyecto

El backend sigue una **arquitectura hexagonal** (puertos y adaptadores):

```
domain/model/          → Entidades de dominio (Actividad, Incidencia, Reserva, Usuario)
business/              → Puertos de servicio (interfaces)
business/impl/         → Implementaciones de los servicios
adapters/in/           → Adaptadores de entrada (controllers REST + DTOs)
adapters/out/          → Adaptadores de salida (persistencia JPA)
adapters/mapper/       → Mappers MapStruct entre capas
infrastructure/        → Configuración de seguridad, JWT y Swagger
```
