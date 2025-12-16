# AdoptaPet

AdoptaPet es una aplicación móvil desarrollada en Android que conecta mascotas en situación de adopción con potenciales adoptantes. Permite publicar mascotas, explorar perfiles, enviar solicitudes de adopción y contactar directamente a los dueños actuales.

## Integrantes

* **Francisca Miranda** - Arquitectura de datos y lógica de negocio
* **Ignacio Sobarzo** - Diseño de interfaz y experiencia de usuario

## Características Principales

La aplicación cuenta con un sistema completo de gestión de adopciones que incluye:

**Autenticación y Usuarios**
- Sistema de registro e inicio de sesión con validación de credenciales
- Gestión de sesiones persistentes con SharedPreferences
- Cada usuario puede ver y administrar sus propias publicaciones

**Gestión de Mascotas**
- Feed principal con filtros por tipo de animal y publicaciones propias
- Publicación de mascotas con captura de foto desde cámara
- Información detallada: nombre, raza, edad, vacunas, descripción y motivo de adopción
- Contador global de adopciones finalizadas

**Proceso de Adopción**
- Formulario completo de solicitud con validación de datos
- Contacto directo vía WhatsApp con mensaje pre-formateado
- Los dueños pueden marcar mascotas como adoptadas

**Funciones Sociales**
- Compartir perfiles de mascotas en redes sociales
- Botón flotante de navegación rápida al inicio del feed
- Interfaz adaptativa con Material Design 3

## Estructura del Proyecto

```
app/
├── model/               # Entidades y acceso a datos
│   ├── entities/       # Room entities (Mascota, Solicitud)
│   ├── dao/            # Data Access Objects
│   ├── api/            # Retrofit API service
│   └── AppDatabase.kt
├── repository/          # Capa de repositorio (fuente única de verdad)
├── viewmodel/          # ViewModels con estados reactivos
├── ui/
│   ├── screens/        # Pantallas Compose
│   ├── navigation/     # Configuración de navegación
│   └── theme/          # Colores y tema visual
└── utils/              # Utilidades (Validaciones, SessionManager)
```

## Tecnologías

**Lenguaje y Framework**
- Kotlin
- Jetpack Compose
- Coroutines y Flow para programación asíncrona

**Arquitectura**
- MVVM (Model-View-ViewModel)
- Repository Pattern
- Room Database con sincronización a backend

**APIs y Servicios**
- Retrofit + Gson para comunicación REST
- Xano como backend remoto
- FileProvider para manejo de imágenes

**UI/UX**
- Material Design 3
- Coil para carga de imágenes
- Navigation Compose
- CameraX para captura de fotos

## Requisitos

- Android Studio Hedgehog o superior
- JDK 11 o superior
- Android API 24+ (Android 7.0+)
- Permisos: Cámara e Internet

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/Ijnacio/AdoptaPet
```

2. Abrir el proyecto en Android Studio

3. Sincronizar Gradle y ejecutar en dispositivo/emulador

## Base de Datos

La app utiliza Room para persistencia local con dos tablas principales:

- **mascota**: Almacena información completa de cada mascota
- **solicitud_adopcion**: Registra las solicitudes de los interesados

La sincronización con Xano permite mantener los datos actualizados entre diferentes usuarios.

## Testing

El proyecto incluye pruebas unitarias en `app/src/test/`:

- `ValidacionesTest.kt`: Validación de correos, contraseñas y montos
- Cobertura de casos límite y entradas inválidas

## Contacto

Para consultas sobre el proyecto, contactar a los desarrolladores mediante el repositorio de GitHub.
