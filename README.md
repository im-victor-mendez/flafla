# 🌿 Flafla

**Flafla** es una aplicación Android e-commerce que facilita la compra de flores y plantas en línea.
Fue desarrollada para brindar una experiencia de compra cómoda, accesible y disfrutable, cubriendo una necesidad actual: la falta de apps de floristería usables y funcionales.

---

## 🎯 Objetivo

Crear una app móvil Android que permita a los usuarios:

- Explorar una tienda de plantas y productos relacionados.
- Comprar fácilmente desde su dispositivo.
- Acceder a métodos de pago, historial de pedidos y opiniones.

---

## 🧑‍💻 Tecnologías

- Android (Java)
- Firebase (Auth, Firestore, Storage)
- GitHub Projects (gestión ágil)
- Glide (carga de imágenes)
- Material Components

---

## 🗂 Estructura del Proyecto

```
com.example.flafla
│
├── 🗂 activities/
├── 🗂 adapters/
├── 🗂 models/
├── 🗂 utils/
├── 🗂 fragments/
```
---

## 🚀 Cómo empezar

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/im-victor-mendez/flafla.git
   cd flafla
   ```

2. Abrir en Android Studio.

3. Conectar con Firebase:

    - Añadir el archivo google-services.json en /app.

4. Configurar Firebase Auth y Firestore.

5. Ejecutar en un emulador o dispositivo físico.

## 🛠 Convenciones de desarrollo
### Ramas:

- main: producción

- dev: integración

- feature/nombre: nueva funcionalidad

- fix/nombre: bugs

### Commits claros, usar convenciones como:

- feat: Nueva funcionalidad

- fix: Corrección de errores

- docs: Cambios en documentación

- refactor: Mejora de código sin cambio funcional

## 📋 Gestión del Proyecto
Usamos GitHub Projects para organizar tareas y flujo de trabajo.

🗂 Columnas sugeridas:
- ✍️ Por hacer

- 🛠 En progreso

- 🔍 En revisión (PRs)

- ✅ Listo

🏷 Etiquetas sugeridas:
- feature

- bug

- UI

- backend

- admin

- alta, media, baja

Cada tarea se representa con un issue conectado a un Pull Request para facilitar la trazabilidad del desarrollo.

## 📄 Documentación
Toda documentación se encuentra en la carpeta /docs, incluyendo:

- firebase_structure.md: esquema de colecciones y subcolecciones

- architecture.md: arquitectura MVVM + Firebase

- setup_guide.md: pasos para correr el proyecto localmente

## 👥 Colaboradores
Victor Mendez - Developer

Jordy Colin - Developer
