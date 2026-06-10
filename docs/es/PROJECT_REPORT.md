# 📄 Memoria Final del Proyecto — CentroPlus Connect

**Ciclo Formativo:** Desarrollo de Aplicaciones Multiplataforma (DAM) — 1.º curso  
**Proyecto:** CentroPlus Connect  
**Repositorio:** https://github.com/Twirkand/CentroPlus-Connect-Nauzet  
**Rama principal:** `main`

---

## 1. Introducción

CentroPlus Connect es una aplicación de gestión desarrollada como proyecto integrador de primer curso de DAM. El sistema cubre las necesidades básicas de un centro educativo o deportivo: gestión de usuarios, actividades, reservas e incidencias.

El proyecto se compone de dos módulos independientes:

- **Backend:** API REST desarrollada con Spring Boot, que expone los servicios del sistema y gestiona la persistencia de datos.
- **Mobile:** Aplicación de escritorio desarrollada con JavaFX, que proporciona la interfaz gráfica de usuario.

---

## 2. Objetivos

- Desarrollar una aplicación funcional aplicando los conocimientos adquiridos durante el primer curso de DAM.
- Implementar una arquitectura limpia y mantenible en el backend siguiendo el patrón hexagonal.
- Ofrecer una interfaz gráfica intuitiva con soporte multiidioma (español, inglés y alemán).
- Aplicar buenas prácticas de desarrollo: control de versiones con Git, tests unitarios, documentación y cobertura de código.

---

## 3. Tecnologías utilizadas

### Backend

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.2.5 | Framework de la API REST |
| Spring Security | — | Autenticación y autorización |
| Spring Data JPA | — | Capa de persistencia |
| H2 Database | — | Base de datos en memoria |
| JWT (jjwt) | 0.11.5 | Gestión de tokens de sesión |
| MapStruct | 1.5.5 | Mapeo entre capas |
| Lombok | 1.18.30 | Reducción de boilerplate |
| SpringDoc OpenAPI | 2.3.0 | Documentación Swagger |
| JaCoCo | 0.8.11 | Cobertura de código |
| JUnit 5 | — | Tests unitarios |

### Mobile

| Tecnología | Uso |
|---|---|
| Java | Lenguaje principal |
| JavaFX + FXML | Interfaz gráfica |
| SQLite | Base de datos embebida local |
| Maven | Gestión de dependencias y build |

---

## 4. Arquitectura del sistema

### Backend — Arquitectura Hexagonal

El backend sigue el patrón de **arquitectura hexagonal** (también conocido como puertos y adaptadores), que separa la lógica de negocio de los detalles de infraestructura.

```
domain/model/          → Entidades de dominio puras
business/              → Interfaces de servicio (puertos)
business/impl/         → Implementaciones de los servicios
adapters/in/           → Controllers REST y DTOs (adaptadores de entrada)
adapters/out/          → Repositorios JPA (adaptadores de salida)
adapters/mapper/       → Mappers MapStruct entre capas
infrastructure/        → Configuración de seguridad, JWT y Swagger
```

Esta arquitectura permite que la lógica de negocio sea completamente independiente del framework y de la base de datos, facilitando los tests y el mantenimiento.

### Mobile — Patrón MVC

La aplicación de escritorio sigue el patrón **MVC (Modelo-Vista-Controlador)**:

- Las vistas se definen en archivos FXML.
- Los controladores JavaFX gestionan los eventos de usuario.
- Los repositorios acceden a la base de datos SQLite local.

---

## 5. Funcionalidades implementadas

### Módulo Backend (API REST)

- **Autenticación:** Login con DNI y contraseña, generación de token JWT.
- **Usuarios:** CRUD completo, búsqueda por DNI, email, tipo y nombre.
- **Actividades:** CRUD completo, filtrado por tipo y precio, gestión de plazas disponibles.
- **Reservas:** CRUD completo, filtrado por usuario, actividad y estado, cancelación.
- **Incidencias:** CRUD completo, filtrado por usuario, estado y asunto, cambio de estado.

### Módulo Mobile (JavaFX)

- Autenticación con login y auto-login mediante token persistente (remember me).
- Gestión de perfil: cambio de email, teléfono y contraseña.
- Visualización y gestión de actividades, reservas e incidencias.
- Soporte multiidioma: español, inglés y alemán.
- Selector de idioma con banderas en la interfaz.
- Estilos visuales unificados (diseño aero glass blue).

---

## 6. Evolución del desarrollo

El proyecto se desarrolló de forma iterativa a lo largo del curso, pasando por las siguientes fases principales:

### Fase 1 — Inicio y estructura base
Se creó la estructura inicial del proyecto, los modelos de datos, la base de datos SQLite y la configuración básica del entorno. Se definieron los primeros modelos de dominio y repositorios.

### Fase 2 — Módulo mobile y lógica de negocio
Se desarrolló la interfaz gráfica con JavaFX, el sistema de login con token persistente, la gestión de sesión global, y los servicios de negocio para usuarios, actividades, reservas e incidencias.

### Fase 3 — Mejoras de interfaz y multiidioma
Se unificaron los estilos CSS a un diseño consistente, se añadió soporte para español, inglés y alemán, y se añadió un selector de idioma con banderas en las vistas principales.

### Fase 4 — Backend Spring Boot
Se inició el módulo backend con Spring Boot, implementando la API REST completa con seguridad JWT, endpoints CRUD para todas las entidades y documentación Swagger.

### Fase 5 — Arquitectura hexagonal
Se refactorizó el backend para adoptar la arquitectura hexagonal, reorganizando los paquetes en dominios, puertos, adaptadores e infraestructura.

### Fase 6 — Tests y cobertura
Se implementaron tests unitarios para los servicios del backend (Actividad, Incidencia, Reserva y Usuario) y tests para los controladores y repositorios del módulo mobile. Se integró JaCoCo para medir la cobertura de código.

### Fase 7 — Documentación
Se redactó documentación completa en español e inglés: documentación de la API REST, guía de instalación y despliegue, diccionario de datos, modelado de base de datos y JavaDoc en servicios y controladores.

---

## 7. Tests

### Backend
Se implementaron tests unitarios con JUnit 5 y Mockito para las implementaciones de los servicios:

- `ActividadServiceImplTest`
- `IncidenciaServiceImplTest`
- `ReservaServiceImplTest`
- `UsuarioServiceImplTest`

La cobertura de código se mide con **JaCoCo**, excluyendo clases sin lógica propia (DTOs, entidades JPA, configuración).

### Mobile
Se implementaron tests para los principales componentes del módulo mobile:

- Tests de controladores: Login, Perfil, CambiarEmail, CambiarTeléfono.
- Tests de repositorios: Reserva, RememberToken, ConnectionManager.
- Tests de utilidades: TokenUtils, PasswordUtils, Validaciones, LanguageManager.
- Tests de sesión y navegación: SessionTest, ScreenManagerTest.

---

## 8. Control de versiones

El proyecto se gestionó con **Git** y **GitHub**, usando la siguiente estrategia de ramas:

| Rama | Uso |
|---|---|
| `main` | Versión estable y releases |
| `develop` | Rama principal de desarrollo |
| `feature/mobile` | Desarrollo del módulo JavaFX |
| `feature/backend` | Desarrollo del módulo Spring Boot |
| `feature/test` | Desarrollo de tests |
| `feature/lang` | Implementación del sistema multiidioma |

Se usó la convención **Conventional Commits** para los mensajes de commit: `feat`, `fix`, `refactor`, `docs`, `test`, `chore`, `style`.

---

## 9. Conclusiones

El desarrollo de CentroPlus Connect ha permitido aplicar de forma práctica los conocimientos adquiridos durante el primer curso de DAM: programación orientada a objetos, acceso a bases de datos, interfaces gráficas, patrones de diseño, APIs REST, seguridad y testing.

Entre los aspectos más destacados del proyecto:

- La adopción de la **arquitectura hexagonal** en el backend, poco habitual en proyectos de primer curso, demuestra una comprensión sólida de los principios de diseño de software.
- El **sistema multiidioma** con soporte para tres idiomas añade un valor significativo a la experiencia de usuario.
- La cobertura de **tests unitarios** en ambos módulos garantiza la fiabilidad del sistema y facilita el mantenimiento futuro.

---

## 10. Posibles mejoras

- Conectar el módulo mobile con la API REST del backend para unificar la capa de datos.
- Añadir roles y permisos diferenciados en la API (administrador, alumno, socio).
- Desarrollar una interfaz web como alternativa al cliente JavaFX.
- Ampliar la cobertura de tests con tests de integración.
