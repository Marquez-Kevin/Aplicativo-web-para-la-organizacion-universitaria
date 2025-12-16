# UPNOTES

# UpNotes 📚🗓️  
Aplicativo web para la organización académica (horario, materias, tareas, calendario y notas).  
Desarrollado con **Spring Boot + Thymeleaf + PostgreSQL**, usando **Java 17** y **Maven**.

---

## ✨ Funcionalidades principales

### ✅ Autenticación
- **Registro** de usuarios
- **Inicio de sesión** (sesión con `HttpSession`)
- **Cerrar sesión**

### ✅ Mis Materias
- Registrar materias con campos **opcionales** (nombre, créditos, semestre, profesor)
- Listar materias registradas
- Eliminar materias
- Crear tareas/actividades por materia:
  - nombre, descripción, fecha límite
  - marcar como pendiente / completada

### ✅ Horario
- Tabla semanal por días y horas
- Agregar materias a cada casilla del horario
- Seleccionar materia desde **“Mis Materias”** (opcional) al agregar al horario
- **Eliminar** materias por casilla del horario
- Conteo de créditos (sin duplicar créditos cuando una misma materia está en varias horas)

### ✅ Calendario
- Muestra calendario del año actual (tipo “Google Calendar” simplificado)
- Se visualizan **solo las tareas pendientes** registradas desde “Mis Materias”
- Los días con tareas pendientes aparecen resaltados

### ✅ Notas
- Registrar actividades y notas asociadas a materias previamente registradas
- Manejo por **3 cortes**
- Cálculo de nota por corte:
  - Corte 1 y 2:
    - promedio(Notas 15%) * 0.15 + (Nota 20%) * 0.20
  - Corte 3:
    - promedio(Notas 15%) * 0.10 + (Nota 20%) * 0.20

---

## 🧰 Tecnologías usadas
- **Java 17**
- **Spring Boot**
- **Thymeleaf**
- **Spring Data JPA**
- **PostgreSQL**
- **Maven**
- **HTML / CSS / JS**

---

## 📂 Estructura del proyecto (backend)

```txt
backend/
 ├─ src/
 │  ├─ main/
 │  │  ├─ java/com/upnotes/backend/
 │  │  │  ├─ controllers/
 │  │  │  ├─ models/
 │  │  │  ├─ repositories/
 │  │  │  └─ BackendApplication.java
 │  │  └─ resources/
 │  │     ├─ static/css/estilos.css
 │  │     ├─ templates/
 │  │     │  ├─ login.html
 │  │     │  ├─ registro.html
 │  │     │  ├─ horario.html
 │  │     │  ├─ materias.html
 │  │     │  ├─ calendario.html
 │  │     │  └─ notas.html
 │  │     └─ application.properties
 │  └─ test/...
 ├─ pom.xml
 ├─ mvnw / mvnw.cmd
 └─ README.md


🗄️ Base de datos (PostgreSQL)

Actualmente el proyecto utiliza una base de datos llamada, por ejemplo:

upnotes_db

Esquema: public

Tablas principales (pueden variar según versión):

usuarios

materias

(y tablas para tareas/notas si aplica)

La conexión se configura en src/main/resources/application.properties.

⚙️ Configuración: application.properties

Ejemplo típico:

spring.datasource.url=jdbc:postgresql://localhost:5432/upnotes_db
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.thymeleaf.cache=false
server.port=8080

▶️ Cómo ejecutar el proyecto
Puedes ejecutar desde BackendApplication.java (botón Run)

🔗 Rutas principales

/ → Login

/registro → Registro

/horario → Horario (requiere sesión)

/materias → Mis Materias (requiere sesión)

/calendario → Calendario (requiere sesión)

/notas → Notas (requiere sesión)

/logout → Cerrar sesión

✅ Reglas de sesión

El usuario autenticado se guarda en sesión como:

usuarioLogueado

Si no hay sesión, se redirige al login.

🧪 Notas y recomendaciones

Si en PowerShell te aparece: "mvnw no se reconoce", ejecuta:

.\mvnw (con el punto y la barra)

Si te aparece Whitelabel Error Page, revisa:

que exista la ruta en el controller

que el archivo .html exista en templates

que el nombre retornado por el controller coincida con el HTML

📌 Autor / Proyecto

UpNotes - Universidad de Pamplona
Sistema de organización académica para estudiantes: horario, materias, tareas, calendario y notas.

JUAN DIEGO SEPULVEDA
KEVIN MARQUEZ
JHON BARRAGAN
