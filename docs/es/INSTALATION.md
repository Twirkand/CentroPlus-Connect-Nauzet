# 🛠️ Instalación y Despliegue — CentroPlus Connect

## Requisitos previos

Asegúrate de tener instalado lo siguiente antes de continuar:

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 |
| Apache Maven | 3.8+ |
| Git | Cualquier versión reciente |

---

## Clonar el repositorio

```bash
git clone https://github.com/Twirkand/CentroPlus-Connect-Nauzet.git
cd CentroPlus-Connect-Nauzet
git checkout develop
```

---

## Backend (Spring Boot)

El backend expone la API REST en `http://localhost:8082`.

### Pasos

```bash
cd backend
mvn spring-boot:run
```

La base de datos H2 se crea automáticamente en memoria al arrancar. No se requiere ninguna configuración adicional de base de datos.

### Autenticación obligatoria

Todos los endpoints de la API requieren autenticación. Antes de hacer cualquier petición debes obtener un token JWT con las siguientes credenciales:

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

Copia el `token` de la respuesta y úsalo en la cabecera de todas las peticiones:

```
Authorization: Bearer <token>
```

En Swagger puedes introducirlo directamente pulsando el botón **Authorize 🔒**.

---

## Mobile (JavaFX)

Aplicación de escritorio desarrollada con JavaFX.

### Pasos

```bash
cd mobile
mvn javafx:run
```

---

## Orden de arranque recomendado

```
1. cd backend  →  mvn spring-boot:run
2. cd mobile   →  mvn javafx:run
```