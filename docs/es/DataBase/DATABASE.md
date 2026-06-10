# Base de datos — CentroPlus Connect



---

## 1. Descripción

La base de datos del proyecto **CentroPlus Connect** está desarrollada en **SQLite3** y tiene como objetivo almacenar toda la información necesaria para la gestión del sistema.

Se trata de una base de datos relacional diseñada para ser utilizada por una aplicación Java (JDBC), una API y una interfaz de usuario.

El modelo ha sido diseñado siguiendo un proceso completo de análisis: **modelo Entidad–Relación (ER), modelo relacional (MR) y normalización hasta 3FN**, garantizando integridad y consistencia de los datos.

---

## 2. Modelado de la base de datos

### 2.1 Modelo Entidad–Relación (ER)

El modelo ER define las entidades principales del sistema y sus relaciones.

<img src="../../../database/IMG/ER.png" width="600">

**Entidades principales:**
- Usuarios
- Actividades
- Reservas
- Incidencias
- Remember_Tokens
- Audit_Log

**Relaciones principales:**
- Un usuario puede realizar múltiples reservas (1:N)
- Una actividad puede tener múltiples reservas (1:N)
- Un usuario puede generar múltiples incidencias (1:N)
- Un usuario puede tener múltiples tokens de autenticación (1:N)

Este modelo permite representar de forma conceptual la estructura del sistema antes de su implementación física.

---

### 2.2 Modelo Relacional (MR)

El modelo relacional transforma el modelo ER en tablas físicas.

<img src="../../../database/IMG/MR.png" width="600">

**Tablas principales:**
- usuarios
- actividades
- reservas
- incidencias
- remember_tokens
- audit_log

**Claves primarias:**
- Todas las tablas utilizan `id` como clave primaria.

**Claves foráneas:**
- reservas.id_usuario → usuarios.id
- reservas.id_actividad → actividades.id
- incidencias.id_usuario → usuarios.id
- remember_tokens.user_id → usuarios.id

Este modelo garantiza la integridad referencial entre las distintas entidades del sistema.

---

### 2.3 Normalización

La base de datos ha sido normalizada hasta **Tercera Forma Normal (3FN)**.

#### 1FN (Primera Forma Normal)
Todos los atributos son atómicos, sin grupos repetidos ni valores multivaluados.

#### 2FN (Segunda Forma Normal)
Todas las tablas utilizan claves primarias simples, por lo que no existen dependencias parciales.

#### 3FN (Tercera Forma Normal)
No existen dependencias transitivas entre atributos no clave. Cada atributo depende únicamente de la clave primaria de su tabla.

---

## 3. Estructura de las tablas

### usuarios
- id (PK)
- nombre
- dni (único)
- email
- telefono
- tipo_usuario
- password
- activo
- created_at
- updated_at

---

### actividades
- id (PK)
- nombre
- tipo_actividad
- duracion
- precio
- plazas_maximas
- plazas_ocupadas
- activo
- created_at
- updated_at

---

### reservas
- id (PK)
- id_usuario (FK)
- id_actividad (FK)
- fecha
- estado
- created_at
- updated_at

---

### incidencias
- id (PK)
- id_usuario (FK)
- asunto
- descripcion
- fecha
- estado
- created_at
- updated_at

---

### remember_tokens
- id (PK)
- user_id (FK)
- token_hash
- expires_at
- created_at

---

### audit_log
- id (PK)
- table_name
- record_id
- action
- old_data
- new_data
- created_at

---

## 4. Validaciones de negocio

### tipo_usuario
- ALUMNO
- SOCIO
- AMBOS

### tipo_actividad
- ACADEMICA
- DEPORTIVA

### estado de reservas
- ACTIVA
- CANCELADA
- COMPLETADA

### estado de incidencias
- ABIERTA
- EN_PROCESO
- CERRADA

---

## 5. Tecnología utilizada

- SQLite3  
- JDBC (Java)  

---

## 6. Ubicación de la base de datos

CentroPlus-Connect/mobile/src/main/resources/database/centroplus.db