# 🐾 AdoptaPet (DSY1105 - Evaluación Parcial 2)

"AdoptaPet" es una aplicación móvil para Android, desarrollada como parte de la Evaluación Parcial 2 de la asignatura Desarrollo de Aplicaciones Móviles (DSY1105).

[cite_start]El proyecto consiste en una interfaz funcional que simula una plataforma para poner mascotas en adopción y permitir que otros usuarios postulen para adoptarlas, integrando los aprendizajes de la asignatura[cite: 12].

---

## 👥 Integrantes

* **Francisca [Apellido]** - *Rol: Arquitecta (Base de Datos & ViewModel)*
* **Ignacio [Apellido]** - *Rol: Diseñador (UI/UX & Recursos Nativos)*

---

## 🚀 Funcionalidades (Versión 1)

[cite_start]Esta versión (V1) del proyecto cumple con los siguientes requisitos obligatorios de la rúbrica [cite: 38-49]:

* **Arquitectura MVVM:** El proyecto está estructurado bajo el patrón Modelo-Vista-ViewModel.
* [cite_start]**Persistencia Local (Room):** La información de las mascotas y las solicitudes de adopción se guardan localmente en una base de datos SQLite mediante Room[cite: 47].
* **UI con Jetpack Compose:** Toda la interfaz de usuario está construida de forma nativa con Jetpack Compose.
* [cite_start]**Navegación Funcional:** Se utiliza `NavHost` (Navigation Compose) para gestionar el flujo entre las 3 pantallas principales[cite: 43].
* [cite_start]**Formularios Validados:** Se incluyen formularios para "Agregar Mascota" y "Solicitar Adopción", con lógica de validación desacoplada de la UI[cite: 44, 45].
* [cite_start]**Recurso Nativo 1 (Cámara):** La app accede a la cámara del dispositivo para tomar una foto al agregar una nueva mascota[cite: 49].
* [cite_start]**Recurso Nativo 2 (Compartir):** La app utiliza el `Intent.ACTION_SEND` del sistema para "compartir" el perfil de una mascota en otras aplicaciones (RRSS, WhatsApp, etc.)[cite: 49].

---

## 🛠️ Pasos para Ejecutar

1.  Clonar este repositorio: `git clone [URL_DEL_REPO]`
2.  Abrir el proyecto en la última versión estable de Android Studio.
3.  Esperar a que Gradle sincronice todas las dependencias (incluyendo Room y Navigation Compose).
4.  Construir (Build) y Ejecutar (Run) el proyecto en un emulador o dispositivo físico.

---

## 💻 Tecnologías Utilizadas

* Kotlin
* Jetpack Compose
* Arquitectura MVVM
* Room (Persistencia Local)
* Navigation Compose
* Material Design 3
* GitHub + Trello (Planificación)
