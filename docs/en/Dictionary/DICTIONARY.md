# 📘 Data Dictionary - CentroPlus Connect

---

## Table: usuarios

| Field        | Type    | Null | Key | Constraints                          |
|--------------|---------|------|-----|--------------------------------------|
| id           | INTEGER | No   | PK  | AUTOINCREMENT                        |
| nombre       | TEXT    | No   |     |                                      |
| dni          | TEXT    | No   | UQ  | UNIQUE                               |
| email        | TEXT    | No   |     |                                      |
| telefono     | TEXT    | Yes  |     |                                      |
| tipo_usuario | TEXT    | No   |     | CHECK(ALUMNO, SOCIO, AMBOS)          |
| password     | TEXT    | No   |     |                                      |
| activo       | INTEGER | No   |     | DEFAULT 1                            |
| created_at   | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |
| updated_at   | TEXT    | Yes  |     | Auto-updated via trigger             |

---

## Table: usuarios_deleted

| Field        | Type    | Null | Key | Description                          |
|--------------|---------|------|-----|--------------------------------------|
| id           | INTEGER | No   | PK  | AUTOINCREMENT                        |
| original_id  | INTEGER | No   |     | Original deleted user ID             |
| nombre       | TEXT    | Yes  |     | Historical data                      |
| dni          | TEXT    | Yes  |     |                                      |
| email        | TEXT    | Yes  |     |                                      |
| telefono     | TEXT    | Yes  |     |                                      |
| tipo_usuario | TEXT    | Yes  |     |                                      |
| password     | TEXT    | Yes  |     |                                      |
| deleted_at   | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |


---

## Table: actividades

| Field           | Type    | Null | Key | Constraints                          |
|-----------------|---------|------|-----|--------------------------------------|
| id              | INTEGER | No   | PK  | AUTOINCREMENT                        |
| nombre          | TEXT    | No   |     |                                      |
| tipo_actividad  | TEXT    | No   |     | CHECK(DEPORTIVA, ACADEMICA)          |
| duracion        | INTEGER | No   |     |                                      |
| precio          | REAL    | No   |     | CHECK(precio >= 0)                   |
| plazas_maximas  | INTEGER | No   |     |                                      |
| plazas_ocupadas | INTEGER | No   |     | DEFAULT 0                            |
| activo          | INTEGER | No   |     | DEFAULT 1                            |
| created_at      | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |
| updated_at      | TEXT    | Yes  |     | Auto-updated via trigger             |

---

## Table: actividades_deleted

| Field           | Type    | Null | Key | Description                          |
|-----------------|---------|------|-----|--------------------------------------|
| id              | INTEGER | No   | PK  | AUTOINCREMENT                        |
| original_id     | INTEGER | No   |     | Original deleted activity ID         |
| nombre          | TEXT    | Yes  |     |                                      |
| tipo_actividad  | TEXT    | Yes  |     |                                      |
| duracion        | INTEGER | Yes  |     |                                      |
| precio          | REAL    | Yes  |     |                                      |
| plazas_maximas  | INTEGER | Yes  |     |                                      |
| plazas_ocupadas | INTEGER | Yes  |     |                                      |
| deleted_at      | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |

---

## Table: reservas

| Field        | Type    | Null | Key | Constraints                          |
|--------------|---------|------|-----|--------------------------------------|
| id           | INTEGER | No   | PK  | AUTOINCREMENT                        |
| id_usuario   | INTEGER | No   | FK  | REFERENCES usuarios(id)              |
| id_actividad | INTEGER | No   | FK  | REFERENCES actividades(id)           |
| fecha        | TEXT    | No   |     |                                      |
| estado       | TEXT    | No   |     | CHECK(ACTIVA, CANCELADA, COMPLETADA) |
| created_at   | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |
| updated_at   | TEXT    | Yes  |     | Auto-updated via trigger             |

---

## Table: reservas_deleted

| Field        | Type    | Null | Key | Description                          |
|--------------|---------|------|-----|--------------------------------------|
| id           | INTEGER | No   | PK  | AUTOINCREMENT                        |
| original_id  | INTEGER | No   |     | Original deleted booking ID          |
| id_usuario   | INTEGER | Yes  |     |                                      |
| id_actividad | INTEGER | Yes  |     |                                      |
| fecha        | TEXT    | Yes  |     |                                      |
| estado       | TEXT    | Yes  |     |                                      |
| deleted_at   | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |

---

## Table: incidencias

| Field       | Type    | Null | Key | Constraints                          |
|-------------|---------|------|-----|--------------------------------------|
| id          | INTEGER | No   | PK  | AUTOINCREMENT                        |
| id_usuario  | INTEGER | No   | FK  | REFERENCES usuarios(id)              |
| asunto      | TEXT    | No   |     |                                      |
| descripcion | TEXT    | No   |     |                                      |
| fecha       | TEXT    | No   |     |                                      |
| estado      | TEXT    | No   |     | CHECK(ABIERTA, EN_PROCESO, CERRADA)  |
| created_at  | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |
| updated_at  | TEXT    | Yes  |     | Auto-updated via trigger             |

---

## Table: incidencias_deleted

| Field       | Type    | Null | Key | Description                          |
|-------------|---------|------|-----|--------------------------------------|
| id          | INTEGER | No   | PK  | AUTOINCREMENT                        |
| original_id | INTEGER | No   |     | Original deleted incident ID         |
| id_usuario  | INTEGER | Yes  |     |                                      |
| asunto      | TEXT    | Yes  |     |                                      |
| descripcion | TEXT    | Yes  |     |                                      |
| fecha       | TEXT    | Yes  |     |                                      |
| estado      | TEXT    | Yes  |     |                                      |
| deleted_at  | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |

---

## Table: remember_tokens

| Field      | Type    | Null | Key | Constraints                          |
|------------|---------|------|-----|--------------------------------------|
| id         | INTEGER | No   | PK  | AUTOINCREMENT                        |
| user_id    | INTEGER | No   | FK  | REFERENCES usuarios(id)              |
| token_hash | TEXT    | No   |     |                                      |
| expires_at | TEXT    | No   |     |                                      |
| created_at | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |

---

## Table: audit_log

| Field      | Type    | Null | Key | Constraints                          |
|------------|---------|------|-----|--------------------------------------|
| id         | INTEGER | No   | PK  | AUTOINCREMENT                        |
| table_name | TEXT    | No   |     |                                      |
| record_id  | INTEGER | No   |     |                                      |
| action     | TEXT    | No   |     | CHECK(INSERT, UPDATE, DELETE)        |
| old_data   | TEXT    | Yes  |     | JSON with previous state             |
| new_data   | TEXT    | Yes  |     | JSON with new state                  |
| created_at | TEXT    | No   |     | DEFAULT CURRENT_TIMESTAMP            |

---

## Triggers

### updated_at Triggers
Automatically update the `updated_at` field on every UPDATE operation.

| Trigger                  | Table       | Event         |
|--------------------------|-------------|---------------|
| trg_usuarios_updated     | usuarios    | AFTER UPDATE  |
| trg_actividades_updated  | actividades | AFTER UPDATE  |
| trg_reservas_updated     | reservas    | AFTER UPDATE  |
| trg_incidencias_updated  | incidencias | AFTER UPDATE  |

### Soft Delete Triggers
Automatically copy the row to the corresponding `_deleted` table before any DELETE operation.

| Trigger                  | Source Table | Target Table         | Copied Fields                                                   |
|--------------------------|--------------|----------------------|-----------------------------------------------------------------|
| trg_usuarios_delete      | usuarios     | usuarios_deleted     | original_id, nombre, dni, email, telefono, tipo_usuario, password |
| trg_actividades_delete   | actividades  | actividades_deleted  | original_id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas |
| trg_reservas_delete      | reservas     | reservas_deleted     | original_id, id_usuario, id_actividad, fecha, estado            |
| trg_incidencias_delete   | incidencias  | incidencias_deleted  | original_id, id_usuario, asunto, descripcion, fecha, estado     |

---

## Foreign Key Relationships

| Table           | Field        | References          |
|-----------------|--------------|---------------------|
| reservas        | id_usuario   | usuarios(id)        |
| reservas        | id_actividad | actividades(id)     |
| incidencias     | id_usuario   | usuarios(id)        |
| remember_tokens | user_id      | usuarios(id)        |

> Foreign key enforcement is enabled via `PRAGMA foreign_keys = ON`.

---

## System Summary

- User management with roles (ALUMNO, SOCIO, AMBOS)
- Sports and academic activities
- Booking system with status control
- Incident management with states
- Persistent login via hashed tokens with expiration
- Full audit log for INSERT / UPDATE / DELETE actions
- Complete soft delete system with historical tables and automatic triggers