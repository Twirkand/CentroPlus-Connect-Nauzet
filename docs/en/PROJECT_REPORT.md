# 📄 Final Project Report — CentroPlus Connect

**Degree:** Cross-Platform Application Development (DAM) — 1st year  
**Project:** CentroPlus Connect  
**Repository:** https://github.com/Twirkand/CentroPlus-Connect-Nauzet  
**Main branch:** `main`

---

## 1. Introduction

CentroPlus Connect is a management application developed as an integrative project for the first year of the DAM degree. The system covers the core needs of an educational or sports centre: user management, activities, bookings and incident tracking.

The project consists of two independent modules:

- **Backend:** REST API built with Spring Boot, exposing the system services and managing data persistence.
- **Mobile:** Desktop application built with JavaFX, providing the graphical user interface.

---

## 2. Objectives

- Develop a functional application applying the knowledge acquired during the first year of DAM.
- Implement a clean and maintainable backend architecture following the hexagonal pattern.
- Provide an intuitive graphical interface with multilanguage support (Spanish, English and German).
- Apply development best practices: version control with Git, unit testing, documentation and code coverage.

---

## 3. Technologies used

### Backend

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Main language |
| Spring Boot | 3.2.5 | REST API framework |
| Spring Security | — | Authentication and authorisation |
| Spring Data JPA | — | Persistence layer |
| H2 Database | — | In-memory database |
| JWT (jjwt) | 0.11.5 | Session token management |
| MapStruct | 1.5.5 | Object mapping between layers |
| Lombok | 1.18.30 | Boilerplate reduction |
| SpringDoc OpenAPI | 2.3.0 | Swagger documentation |
| JaCoCo | 0.8.11 | Code coverage |
| JUnit 5 | — | Unit testing |

### Mobile

| Technology | Purpose |
|---|---|
| Java | Main language |
| JavaFX + FXML | Graphical interface |
| SQLite | Local embedded database |
| Maven | Dependency management and build |

---

## 4. System architecture

### Backend — Hexagonal Architecture

The backend follows the **hexagonal architecture** pattern (also known as ports and adapters), which separates business logic from infrastructure details.

```
domain/model/          → Pure domain entities
business/              → Service interfaces (ports)
business/impl/         → Service implementations
adapters/in/           → REST controllers and DTOs (inbound adapters)
adapters/out/          → JPA repositories (outbound adapters)
adapters/mapper/       → MapStruct mappers between layers
infrastructure/        → Security, JWT and Swagger configuration
```

This architecture keeps business logic fully independent of the framework and the database, making testing and maintenance easier.

### Mobile — MVC Pattern

The desktop application follows the **MVC (Model-View-Controller)** pattern:

- Views are defined in FXML files.
- JavaFX controllers handle user events.
- Repositories access the local SQLite database.

---

## 5. Implemented features

### Backend module (REST API)

- **Authentication:** Login with DNI and password, JWT token generation.
- **Users:** Full CRUD, search by DNI, email, type and name.
- **Activities:** Full CRUD, filtering by type and price, available spot management.
- **Bookings:** Full CRUD, filtering by user, activity and status, cancellation.
- **Incidents:** Full CRUD, filtering by user, status and subject, status change.

### Mobile module (JavaFX)

- Authentication with login and auto-login via persistent token (remember me).
- Profile management: change email, phone and password.
- View and manage activities, bookings and incidents.
- Multilanguage support: Spanish, English and German.
- Language selector with flag icons in the interface.
- Unified visual styles (aero glass blue design).

---

## 6. Development timeline

The project was developed iteratively throughout the course, going through the following main phases:

### Phase 1 — Initial setup
The initial project structure was created along with the data models, the SQLite database and the basic environment configuration. The first domain models and repositories were defined.

### Phase 2 — Mobile module and business logic
The JavaFX graphical interface was developed, including the login system with persistent token, global session management, and the business services for users, activities, bookings and incidents.

### Phase 3 — UI improvements and multilanguage support
CSS styles were unified into a consistent design, support for Spanish, English and German was added, and a flag-based language selector was introduced in the main views.

### Phase 4 — Spring Boot backend
The backend module was started with Spring Boot, implementing the full REST API with JWT security, CRUD endpoints for all entities and Swagger documentation.

### Phase 5 — Hexagonal architecture
The backend was refactored to adopt the hexagonal architecture, reorganising packages into domain, ports, adapters and infrastructure layers.

### Phase 6 — Testing and coverage
Unit tests were implemented for the backend service layer (Actividad, Incidencia, Reserva and Usuario) and for the controllers and repositories of the mobile module. JaCoCo was integrated to measure code coverage.

### Phase 7 — Documentation
Full documentation was written in both Spanish and English: REST API reference, installation and deployment guide, data dictionary, database modelling and JavaDoc on services and controllers.

---

## 7. Testing

### Backend
Unit tests were implemented with JUnit 5 and Mockito for the service layer implementations:

- `ActividadServiceImplTest`
- `IncidenciaServiceImplTest`
- `ReservaServiceImplTest`
- `UsuarioServiceImplTest`

Code coverage is measured with **JaCoCo**, excluding classes with no business logic (DTOs, JPA entities, configuration).

### Mobile
Tests were implemented for the main components of the mobile module:

- Controller tests: Login, Perfil, CambiarEmail, CambiarTeléfono.
- Repository tests: Reserva, RememberToken, ConnectionManager.
- Utility tests: TokenUtils, PasswordUtils, Validaciones, LanguageManager.
- Session and navigation tests: SessionTest, ScreenManagerTest.

---

## 8. Version control

The project was managed with **Git** and **GitHub**, using the following branching strategy:

| Branch | Purpose |
|---|---|
| `main` | Stable version and releases |
| `develop` | Main development branch |
| `feature/mobile` | JavaFX module development |
| `feature/backend` | Spring Boot module development |
| `feature/test` | Test development |
| `feature/lang` | Multilanguage system implementation |

The **Conventional Commits** convention was used for commit messages: `feat`, `fix`, `refactor`, `docs`, `test`, `chore`, `style`.

---

## 9. Conclusions

The development of CentroPlus Connect has allowed the practical application of knowledge acquired during the first year of DAM: object-oriented programming, database access, graphical interfaces, design patterns, REST APIs, security and testing.

Among the most notable aspects of the project:

- The adoption of **hexagonal architecture** in the backend, uncommon in first-year projects, demonstrates a solid understanding of software design principles.
- The **multilanguage system** with support for three languages adds significant value to the user experience.
- **Unit test coverage** across both modules ensures the reliability of the system and facilitates future maintenance.

---

## 10. Future improvements

- Connect the mobile module to the backend REST API to unify the data layer.
- Add differentiated roles and permissions to the API (admin, student, member).
- Develop a web interface as an alternative to the JavaFX client.
- Expand test coverage with integration tests..
