PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS incidencias_deleted;
DROP TABLE IF EXISTS reservas_deleted;
DROP TABLE IF EXISTS actividades_deleted;
DROP TABLE IF EXISTS usuarios_deleted;

DROP TABLE IF EXISTS remember_tokens;
DROP TABLE IF EXISTS audit_log;

DROP TABLE IF EXISTS incidencias;
DROP TABLE IF EXISTS reservas;
DROP TABLE IF EXISTS actividades;
DROP TABLE IF EXISTS usuarios;


-- USUARIOS

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    dni TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL,
    telefono TEXT,
    tipo_usuario TEXT NOT NULL CHECK(tipo_usuario IN ('ALUMNO','SOCIO','AMBOS','ADMIN')),
    password TEXT NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT
);

CREATE TABLE usuarios_deleted (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_id INTEGER NOT NULL,
    nombre TEXT,
    dni TEXT,
    email TEXT,
    telefono TEXT,
    tipo_usuario TEXT,
    password TEXT,
    deleted_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ACTIVIDADES

CREATE TABLE actividades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    tipo_actividad TEXT NOT NULL CHECK(tipo_actividad IN ('DEPORTIVA','ACADEMICA')),
    duracion INTEGER NOT NULL,
    precio REAL NOT NULL CHECK(precio >= 0),
    plazas_maximas INTEGER NOT NULL,
    plazas_ocupadas INTEGER NOT NULL DEFAULT 0,
    activo INTEGER NOT NULL DEFAULT 1,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT
);

CREATE TABLE actividades_deleted (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_id INTEGER NOT NULL,
    nombre TEXT,
    tipo_actividad TEXT,
    duracion INTEGER,
    precio REAL,
    plazas_maximas INTEGER,
    plazas_ocupadas INTEGER,
    deleted_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- RESERVAS

CREATE TABLE reservas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    id_actividad INTEGER NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL CHECK(estado IN ('ACTIVA','CANCELADA','COMPLETADA')),
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_actividad) REFERENCES actividades(id)
);

CREATE TABLE reservas_deleted (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_id INTEGER NOT NULL,
    id_usuario INTEGER,
    id_actividad INTEGER,
    fecha TEXT,
    estado TEXT,
    deleted_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- INCIDENCIAS

CREATE TABLE incidencias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    asunto TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha TEXT NOT NULL,
    estado TEXT NOT NULL CHECK(estado IN ('ABIERTA','EN_PROCESO','CERRADA')),
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE incidencias_deleted (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    original_id INTEGER NOT NULL,
    id_usuario INTEGER,
    asunto TEXT,
    descripcion TEXT,
    fecha TEXT,
    estado TEXT,
    deleted_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- REMEMBER TOKENS

CREATE TABLE remember_tokens (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    token_hash TEXT NOT NULL,
    expires_at TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES usuarios(id)
);


-- AUDIT LOG

CREATE TABLE audit_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_name TEXT NOT NULL,
    record_id INTEGER NOT NULL,
    action TEXT NOT NULL CHECK(action IN ('INSERT','UPDATE','DELETE')),
    old_data TEXT,
    new_data TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- UPDATED_AT TRIGGERS


CREATE TRIGGER trg_usuarios_updated
AFTER UPDATE ON usuarios
FOR EACH ROW
BEGIN
    UPDATE usuarios
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.id;
END;

CREATE TRIGGER trg_actividades_updated
AFTER UPDATE ON actividades
FOR EACH ROW
BEGIN
    UPDATE actividades
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.id;
END;

CREATE TRIGGER trg_reservas_updated
AFTER UPDATE ON reservas
FOR EACH ROW
BEGIN
    UPDATE reservas
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.id;
END;

CREATE TRIGGER trg_incidencias_updated
AFTER UPDATE ON incidencias
FOR EACH ROW
BEGIN
    UPDATE incidencias
    SET updated_at = CURRENT_TIMESTAMP
    WHERE id = OLD.id;
END;


-- SOFT DELETE TRIGGERS


CREATE TRIGGER trg_usuarios_delete
BEFORE DELETE ON usuarios
FOR EACH ROW
BEGIN
    INSERT INTO usuarios_deleted(
        original_id, nombre, dni, email, telefono, tipo_usuario, password
    )
    VALUES(
        OLD.id, OLD.nombre, OLD.dni, OLD.email, OLD.telefono, OLD.tipo_usuario, OLD.password
    );
END;

CREATE TRIGGER trg_actividades_delete
BEFORE DELETE ON actividades
FOR EACH ROW
BEGIN
    INSERT INTO actividades_deleted(
        original_id, nombre, tipo_actividad, duracion, precio, plazas_maximas, plazas_ocupadas
    )
    VALUES(
        OLD.id, OLD.nombre, OLD.tipo_actividad, OLD.duracion, OLD.precio, OLD.plazas_maximas, OLD.plazas_ocupadas
    );
END;

CREATE TRIGGER trg_reservas_delete
BEFORE DELETE ON reservas
FOR EACH ROW
BEGIN
    INSERT INTO reservas_deleted(
        original_id, id_usuario, id_actividad, fecha, estado
    )
    VALUES(
        OLD.id, OLD.id_usuario, OLD.id_actividad, OLD.fecha, OLD.estado
    );
END;

CREATE TRIGGER trg_incidencias_delete
BEFORE DELETE ON incidencias
FOR EACH ROW
BEGIN
    INSERT INTO incidencias_deleted(
        original_id, id_usuario, asunto, descripcion, fecha, estado
    )
    VALUES(
        OLD.id, OLD.id_usuario, OLD.asunto, OLD.descripcion, OLD.fecha, OLD.estado
    );
END;