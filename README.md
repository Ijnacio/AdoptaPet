# 🐾 AdoptaPet (DSY1105 - Evaluación Parcial 2)

AdoptaPet es una aplicación móvil para Android que simula una plataforma funcional para poner mascotas en adopción y permitir que usuarios postulen para adoptarlas. El proyecto integra los conceptos clave de desarrollo móvil moderno con Kotlin y Jetpack Compose.

---

## 👥 Integrantes

* **Francisca Miranda ** - *Rol: Arquitecta (Base de Datos & ViewModel)*
* **Ignacio Sobarzo ** - *Rol: Diseñador (UI/UX & Recursos Nativos)*

---

## 🚀 Funcionalidades (Versión 1)

[cite_start]Esta versión (V1) del proyecto cumple con los siguientes requisitos obligatorios de la rúbrica [cite: 38-49]:

- Arquitectura MVVM: Implementación completa del patrón Modelo-Vista-ViewModel para separación de responsabilidades
- Persistencia Local con Room: Base de datos SQLite local para almacenar mascotas y solicitudes de adopción
- UI Moderna con Jetpack Compose: Interfaz nativa completamente construida con Compose y Material Design 3
- Navegación Fluida: Sistema de navegación multi-pantalla implementado con Navigation Compose
- Formularios Validados: Validación de datos desacoplada para agregar mascotas y solicitar adopciones
- Integración de Cámara: Captura de fotos directamente desde la app al agregar nuevas mascotas
- Función Compartir: Comparte perfiles de mascotas en redes sociales y aplicaciones de mensajería

---

## Pantallas

1. Lista de Mascotas: Visualización de todas las mascotas disponibles para adopción
2. Detalle de Mascota: Información completa con opción de solicitar adopción
3. Agregar Mascota: Formulario con captura de foto para registrar nuevas mascotas

---

## 🛠️ Pasos para Ejecutar

1. Android Studio (última versión estable)
2. JDK 11 o superior
3. Dispositivo Android o emulador con API 24+
4. Clonar este repositorio: `https://github.com/Ijnacio/AdoptaPet`


---

## 💻 Tecnologías Utilizadas

Stack Principal

- Kotlin - Lenguaje de programación
- Jetpack Compose - Framework UI moderno
- Material Design 3 - Sistema de diseño

Arquitectura y Persistencia

- MVVM - Patrón arquitectónico
- Room 2.6.1 - Base de datos local SQLite
- Navigation Compose 2.7.7 - Navegación entre pantallas

Dependencias Adicionales

- Coil 2.6.0 - Carga y visualización de imágenes
- Lifecycle Runtime KTX - Manejo de ciclo de vida
- Activity Compose - Integración con Activities
