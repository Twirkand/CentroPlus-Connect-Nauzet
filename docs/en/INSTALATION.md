# 🛠️ Installation & Deployment — CentroPlus Connect

## Prerequisites

Make sure the following tools are installed before proceeding:

| Tool | Minimum version |
|---|---|
| Java JDK | 17 |
| Apache Maven | 3.8+ |
| Git | Any recent version |

---

## Clone the repository

```bash
git clone https://github.com/Twirkand/CentroPlus-Connect-Nauzet.git
cd CentroPlus-Connect-Nauzet
git checkout develop
```

---

## Backend (Spring Boot)

The backend exposes the REST API at `http://localhost:8082`.

### Steps

```bash
cd backend
mvn spring-boot:run
```

The H2 database is created automatically in memory on startup. No additional database configuration is required.

### Mandatory authentication

All API endpoints require authentication. Before making any request you must obtain a JWT token using the following credentials:

- **DNI:** `99999999X`
- **Password:** `admin123`

```bash
POST http://localhost:8080/api/v1/auth/login
Content-Type: application/json

{
  "dni": "99999999X",
  "password": "admin123"
}
```

Copy the `token` from the response and include it in the header of every request:

```
Authorization: Bearer <token>
```

In Swagger you can enter it directly by clicking the **Authorize 🔒** button.

---

## Mobile (JavaFX)

Desktop application developed with JavaFX.

### Steps

```bash
cd mobile
mvn javafx:run
```

---

## Recommended startup order

```
1. cd backend  →  mvn spring-boot:run
2. cd mobile   →  mvn javafx:run
```