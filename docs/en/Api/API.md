# CentroPlus Connect — API Documentation

## General Information

| Field | Value |
|---|---|
| Version | `0.0.1-SNAPSHOT` |
| Base URL | `http://localhost:8080/api/v1` |
| Format | JSON |
| Authentication | JWT Bearer Token |
| Interactive docs | `http://localhost:8080/swagger-ui.html` |

### Tech Stack

- **Java 17** · Spring Boot 3.2.5
- **Spring Security** + JWT (jjwt 0.11.5)
- **Spring Data JPA** · H2 in-memory database
- **Hexagonal architecture** (ports and adapters)
- **MapStruct** for object mapping · **Lombok** for boilerplate reduction
- **SpringDoc OpenAPI 2.3.0** for Swagger documentation

---

## Authentication

All endpoints (except `/auth/login`) require a JWT token in the request header.

```
Authorization: Bearer <token>
```

### POST `/api/v1/auth/login`

Logs in with DNI and password and returns a JWT token.

**Request body:**
```json
{
  "dni": "12345678A",
  "password": "password"
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

**Response `401 Unauthorized`:** Invalid credentials.

---

## Users

Base path: `/api/v1/usuarios`

### User types

| Value | Description |
|---|---|
| `ALUMNO` | Student |
| `SOCIO` | Member |
| `AMBOS` | Both student and member |

### Endpoints

#### `GET /api/v1/usuarios`
Returns a list of all registered users.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Juan García",
    "dni": "12345678A",
    "email": "juan@example.com",
    "tipoUsuario": "ALUMNO"
  }
]
```

---

#### `GET /api/v1/usuarios/{id}`
Returns a user by ID.

| Parameter | Type | Description |
|---|---|---|
| `id` | `int` (path) | User ID |

---

#### `GET /api/v1/usuarios/dni/{dni}`
Finds a user by DNI.

| Parameter | Type | Description |
|---|---|---|
| `dni` | `String` (path) | User's DNI |

---

#### `GET /api/v1/usuarios/email/{email}`
Finds a user by email address.

| Parameter | Type | Description |
|---|---|---|
| `email` | `String` (path) | User's email |

---

#### `GET /api/v1/usuarios/tipo/{tipo}`
Returns users filtered by type.

| Parameter | Type | Allowed values |
|---|---|---|
| `tipo` | `String` (path) | `ALUMNO`, `SOCIO`, `AMBOS` |

---

#### `GET /api/v1/usuarios/buscar?nombre={nombre}`
Partial name search across all users (case-insensitive).

| Parameter | Type | Description |
|---|---|---|
| `nombre` | `String` (query) | Name fragment to search |

---

#### `POST /api/v1/usuarios?password={password}`
Creates a new user.

| Parameter | Type | Description |
|---|---|---|
| `password` | `String` (query) | Plain-text password (stored BCrypt-hashed) |

**Request body:**
```json
{
  "nombre": "Juan García",
  "dni": "12345678A",
  "email": "juan@example.com",
  "tipoUsuario": "ALUMNO"
}
```

**Response `200 OK`:** The created user.

---

#### `PUT /api/v1/usuarios/{id}`
Updates an existing user.

**Request body:** same as `POST`.

---

#### `DELETE /api/v1/usuarios/{id}`
Deletes a user by ID.

**Response `204 No Content`**

---

## Activities

Base path: `/api/v1/actividades`

### Activity types

| Value | Description |
|---|---|
| `DEPORTIVA` | Sports activity |
| `ACADEMICA` | Academic activity |

### Endpoints

#### `GET /api/v1/actividades`
Returns a list of all activities.

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
Returns an activity by ID.

---

#### `GET /api/v1/actividades/tipo/{tipo}`
Filters activities by type.

| Parameter | Type | Allowed values |
|---|---|---|
| `tipo` | `String` (path) | `DEPORTIVA`, `ACADEMICA` |

---

#### `GET /api/v1/actividades/buscar?nombre={nombre}`
Partial name search across activities.

| Parameter | Type | Description |
|---|---|---|
| `nombre` | `String` (query) | Name fragment to search |

---

#### `GET /api/v1/actividades/precio?max={max}`
Returns activities priced at or below the given maximum.

| Parameter | Type | Description |
|---|---|---|
| `max` | `double` (query) | Maximum price |

---

#### `POST /api/v1/actividades`
Creates a new activity.

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
Updates an existing activity.

**Request body:** same as `POST`.

---

#### `DELETE /api/v1/actividades/{id}`
Deletes an activity.

**Response `204 No Content`**

---

#### `PATCH /api/v1/actividades/{id}/reservar-plaza`
Decrements the available spots for the activity by one.

**Response `200 OK`:**
```json
true
```

Returns `false` if no spots are available.

---

#### `PATCH /api/v1/actividades/{id}/cancelar-plaza`
Increments the available spots for the activity by one.

**Response `200 OK`:**
```json
true
```

---

## Bookings

Base path: `/api/v1/reservas`

### Booking states

| Value | Description |
|---|---|
| `ACTIVA` | Active booking |
| `CANCELADA` | Cancelled booking |
| `COMPLETADA` | Completed booking |

### Endpoints

#### `GET /api/v1/reservas`
Returns a list of all bookings.

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
Returns a booking by ID.

---

#### `GET /api/v1/reservas/usuario/{idUsuario}`
Returns all bookings belonging to a user.

| Parameter | Type | Description |
|---|---|---|
| `idUsuario` | `int` (path) | User ID |

---

#### `GET /api/v1/reservas/actividad/{idActividad}`
Returns all bookings for a given activity.

| Parameter | Type | Description |
|---|---|---|
| `idActividad` | `int` (path) | Activity ID |

---

#### `GET /api/v1/reservas/estado/{estado}`
Filters bookings by state.

| Parameter | Type | Allowed values |
|---|---|---|
| `estado` | `String` (path) | `ACTIVA`, `CANCELADA`, `COMPLETADA` |

---

#### `POST /api/v1/reservas`
Creates a new booking.

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
Updates an existing booking.

**Request body:** same as `POST`.

---

#### `DELETE /api/v1/reservas/{id}`
Deletes a booking.

**Response `204 No Content`**

---

#### `PATCH /api/v1/reservas/{id}/estado?estado={estado}`
Changes the state of a booking.

| Parameter | Type | Allowed values |
|---|---|---|
| `estado` | `String` (query) | `ACTIVA`, `CANCELADA`, `COMPLETADA` |

**Response `200 OK`:** `true` if the change was successful.

---

#### `PATCH /api/v1/reservas/{id}/cancelar?idUsuario={idUsuario}`
Cancels a booking for a specific user.

| Parameter | Type | Description |
|---|---|---|
| `idUsuario` | `int` (query) | ID of the booking owner |

**Response `200 OK`:** `true` if the cancellation was successful.

---

## Incidents

Base path: `/api/v1/incidencias`

### Incident states

| Value | Description |
|---|---|
| `ABIERTA` | Newly reported incident |
| `EN_PROCESO` | Incident being handled |
| `CERRADA` | Resolved incident |

### Endpoints

#### `GET /api/v1/incidencias`
Returns a list of all incidents.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "idUsuario": 1,
    "asunto": "Access problem",
    "descripcion": "Cannot access the members area",
    "estado": "ABIERTA"
  }
]
```

---

#### `GET /api/v1/incidencias/{id}`
Returns an incident by ID.

---

#### `GET /api/v1/incidencias/usuario/{idUsuario}`
Returns all incidents reported by a user.

| Parameter | Type | Description |
|---|---|---|
| `idUsuario` | `int` (path) | User ID |

---

#### `GET /api/v1/incidencias/estado/{estado}`
Filters incidents by state.

| Parameter | Type | Allowed values |
|---|---|---|
| `estado` | `String` (path) | `ABIERTA`, `EN_PROCESO`, `CERRADA` |

---

#### `GET /api/v1/incidencias/buscar?asunto={asunto}`
Partial search of incidents by subject.

| Parameter | Type | Description |
|---|---|---|
| `asunto` | `String` (query) | Subject fragment to search |

---

#### `POST /api/v1/incidencias`
Creates a new incident report.

**Request body:**
```json
{
  "idUsuario": 1,
  "asunto": "Access problem",
  "descripcion": "Cannot access the members area",
  "estado": "ABIERTA"
}
```

---

#### `PUT /api/v1/incidencias/{id}`
Updates an existing incident.

**Request body:** same as `POST`.

---

#### `DELETE /api/v1/incidencias/{id}`
Deletes an incident.

**Response `204 No Content`**

---

#### `PATCH /api/v1/incidencias/{id}/estado?estado={estado}`
Changes the state of an incident.

| Parameter | Type | Allowed values |
|---|---|---|
| `estado` | `String` (query) | `ABIERTA`, `EN_PROCESO`, `CERRADA` |

**Response `200 OK`:** `true` if the change was successful.

---

## HTTP Response Codes

| Code | Description |
|---|---|
| `200 OK` | Successful operation |
| `204 No Content` | Successful deletion |
| `401 Unauthorized` | Missing or invalid token |
| `404 Not Found` | Resource not found |
| `500 Internal Server Error` | Unexpected server error |

---

## Project Architecture

The backend follows a **hexagonal architecture** (ports and adapters pattern):

```
domain/model/          → Domain entities (Actividad, Incidencia, Reserva, Usuario)
business/              → Service ports (interfaces)
business/impl/         → Service implementations
adapters/in/           → Inbound adapters (REST controllers + DTOs)
adapters/out/          → Outbound adapters (JPA persistence)
adapters/mapper/       → MapStruct mappers between layers
infrastructure/        → Security config, JWT and Swagger setup
```
