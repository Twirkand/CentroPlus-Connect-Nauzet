# 📘 CentroPlus Connect — Documentación del Proyecto

---

## 📑 Índice

1. [Documentación funcional](#1-documentación-funcional)
   - 1.1 [Propósito del sistema](#11-propósito-del-sistema)
   - 1.2 [Roles de usuario](#12-roles-de-usuario)
   - 1.3 [Funcionalidades principales](#13-funcionalidades-principales)
2. [Casos de uso](#2-casos-de-uso)
   - 2.1 [Login](#21-login)
   - 2.2 [Logout](#22-logout)
   - 2.3 [Crear reserva](#23-crear-reserva)
   - 2.4 [Cancelar reserva](#24-cancelar-reserva)
   - 2.5 [Crear incidencia](#25-crear-incidencia)
3. [Arquitectura del sistema](#3-arquitectura-del-sistema)
   - 3.1 [Capas](#31-capas)
   - 3.2 [Flujo de arquitectura](#32-flujo-de-arquitectura)
4. [Seguridad](#4-seguridad)
   - 4.1 [Seguridad de contraseñas](#41-seguridad-de-contraseñas)
   - 4.2 [Sistema remember-me](#42-sistema-remember-me)
   - 4.3 [Gestión de sesión](#43-gestión-de-sesión)
5. [Guía de instalación](#5-guía-de-instalación)
   - 5.1 [Requisitos](#51-requisitos)
   - 5.2 [Pasos de ejecución](#52-pasos-de-ejecución)
   - 5.3 [Configuración de base de datos](#53-configuración-de-base-de-datos)
6. [Manual de usuario](#6-manual-de-usuario)
   - 6.1 [Login](#61-login)
   - 6.2 [Pantalla principal](#62-pantalla-principal)
   - 6.3 [Perfil](#63-perfil)
7. [Plan de pruebas](#7-plan-de-pruebas)
   - 7.1 [Pruebas de login](#71-pruebas-de-login)
   - 7.2 [Pruebas de reservas](#72-pruebas-de-reservas)
   - 7.3 [Pruebas de incidencias](#73-pruebas-de-incidencias)
8. [Arquitectura del código](#8-arquitectura-del-código-paquetes-java)
9. [Flujo de login](#9-flujo-de-login)
10. [Cobertura de Tests con JaCoCo](#10--cobertura-de-tests-con-jacoco)
11. [Resumen](#11-resumen)

---

# 1. Documentación funcional

## 1.1 Propósito del sistema

CentroPlus Connect es un sistema de gestión diseñado para administrar usuarios, actividades, reservas e incidencias dentro de una plataforma organizada.

Permite a los usuarios:
- Registrarse y autenticarse
- Reservar actividades
- Gestionar reservas
- Reportar incidencias
- Mantener una sesión persistente (sistema remember-me)

---

## 1.2 Roles de usuario

### Estudiante (ALUMNO)
- Ver actividades
- Crear reservas
- Reportar incidencias

### Socio (SOCIO)
- Mismos permisos que un estudiante
- Acceso a actividades adicionales si están disponibles

### Ambos (AMBOS)
- Combinación de los permisos de estudiante y socio

---

## 1.3 Funcionalidades principales

- Autenticación de usuarios (login/logout)
- Inicio de sesión persistente (tokens remember-me)
- Gestión de actividades
- Sistema de reservas
- Sistema de incidencias
- Gestión de perfil de usuario
- Control de sesión

---

# 2. Casos de uso

## 2.1 Login

**Actor:** Usuario  
**Precondición:** El usuario existe en la base de datos  

**Flujo principal:**
1. El usuario introduce DNI y contraseña
2. El sistema valida las credenciales
3. Si son correctas, se crea la sesión
4. Opcional: se genera token remember-me

**Errores:**
- Credenciales inválidas
- Campos vacíos

---

## 2.2 Logout

**Actor:** Usuario autenticado  
**Precondición:** Usuario logueado  

**Flujo:**
1. El sistema elimina la sesión
2. Borra el token almacenado (si existe)
3. Redirige a la pantalla de login

---

## 2.3 Crear reserva

**Actor:** Usuario  
**Precondición:** Usuario autenticado  

**Flujo:**
1. El usuario selecciona una actividad
2. El sistema verifica disponibilidad
3. Se crea la reserva
4. Se actualizan las plazas de la actividad

---

## 2.4 Cancelar reserva

**Actor:** Usuario  
**Precondición:** La reserva existe  

**Flujo:**
1. El usuario selecciona la reserva
2. El sistema la marca como cancelada
3. Se restauran las plazas de la actividad

---

## 2.5 Crear incidencia

**Actor:** Usuario  
**Precondición:** Usuario autenticado  

**Flujo:**
1. El usuario envía el formulario de incidencia
2. El sistema la guarda en la base de datos
3. El estado se establece como ABIERTA

---

# 3. Arquitectura del sistema

## 3.1 Capas

El sistema sigue una arquitectura en capas:

- Capa de presentación → Controladores JavaFX
- Capa de servicio → Lógica de negocio
- Capa de repositorio → Acceso a base de datos (JDBC)
- Capa de utilidades → Sesión, tokens, navegación

---

## 3.2 Flujo de arquitectura

```
Interfaz (JavaFX)
↓
Controlador
↓
Servicio
↓
Repositorio
↓
Base de datos SQLite
```

---

# 4. Seguridad

## 4.1 Seguridad de contraseñas
- Las contraseñas se almacenan hasheadas (SHA-256 o equivalente)
- No se almacenan contraseñas en texto plano

---

## 4.2 Sistema remember-me
- Los tokens se generan al iniciar sesión
- Se almacenan hasheados
- Tienen fecha de expiración
- Se guardan en la tabla `remember_tokens`

---

## 4.3 Gestión de sesión
- El usuario activo se guarda en memoria (clase `Session`)
- El ID del usuario se almacena en preferencias del sistema
- La sesión se elimina al hacer logout

---

# 5. Guía de instalación

## 5.1 Requisitos
- Java 17 o superior
- Maven
- SQLite
- JavaFX

---

## 5.2 Pasos de ejecución

1. Clonar el repositorio
2. Abrir el proyecto en un IDE
3. Compilar con Maven
4. Ejecutar la clase `Main`

---

## 5.3 Configuración de base de datos

Ubicación del archivo:
```
mobile/src/main/resources/database/centroplus.db
```

No requiere configuración manual (SQLite incluido).

---

# 6. Manual de usuario

## 6.1 Login
- Introducir DNI y contraseña
- Opcional activar "Recordarme"

## 6.2 Pantalla principal
- Ver actividades disponibles
- Acceder a reservas
- Gestionar incidencias

## 6.3 Perfil
- Ver datos personales
- Cambiar contraseña
- Cerrar sesión

---

# 7. Plan de pruebas

## 7.1 Pruebas de login
- Credenciales válidas → éxito
- DNI vacío → error
- Contraseña vacía → error
- Contraseña incorrecta → error
- Usuario inexistente → error

---

## 7.2 Pruebas de reservas
- Crear reserva correctamente
- Cancelar reserva correctamente
- Sin plazas disponibles → error

---

## 7.3 Pruebas de incidencias
- Crear incidencia correctamente
- Campos vacíos → error de validación

---

# 8. Arquitectura del código (paquetes Java)

- `controllers` → lógica de interfaz
- `services` → lógica de negocio
- `repositories` → acceso a datos
- `models` → entidades
- `utils` → utilidades (Session, ScreenManager, TokenUtils)

---

# 9. Flujo de login

```
Entrada del usuario
↓
Validación de credenciales
↓
Consulta en repositorio
↓
Verificación de contraseña
↓
Creación de sesión
↓
Generación opcional de token
↓
Redirección a pantalla principal
```

---

# 10. Cobertura de Tests con JaCoCo

En **CentroPlus Connect** hemos puesto especial énfasis en la calidad del código mediante la implementación de una suite de tests exhaustiva. Para garantizar la fiabilidad del sistema, hemos utilizado **JaCoCo (Java Code Coverage)** como herramienta de análisis de cobertura, lo que nos ha permitido identificar y cubrir los puntos críticos de la aplicación.

## ¿Qué hemos testeado?

Se han desarrollado tests unitarios e de integración sobre los principales módulos del proyecto:

- **Controladores** (`dam.mod.controllers`) — lógica de entrada y manejo de peticiones
- **Servicios** (`dam.mod.services.impl`) — reglas de negocio de la aplicación
- **Utilidades** (`dam.mod.utils`) — funciones auxiliares y helpers
- **Modelos** (`dam.mod.models`) — entidades y objetos del dominio
- **Repositorios** (`dam.mod.repositories`) — capa de acceso a datos (SQLite e implementaciones)

## Resultados obtenidos

Los resultados del análisis muestran una **cobertura global del 92% en instrucciones** y un **82% en ramas**, lo que refleja un alto nivel de confianza en el comportamiento del sistema ante distintos escenarios.

A continuación se muestra el informe generado por JaCoCo:

![Informe de cobertura JaCoCo](./IMG/test.png)

> *Informe generado con JaCoCo 0.8.12 · Fecha: 2026-06-05*

---

# 11. Resumen

CentroPlus Connect es una aplicación Java en capas que integra:

- Sistema de autenticación
- Gestión de actividades
- Sistema de reservas
- Gestión de incidencias
- Login persistente con tokens
- Persistencia con SQLite