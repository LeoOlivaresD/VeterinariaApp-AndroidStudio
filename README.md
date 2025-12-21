# 🐾 Veterinaria App - Sistema de Gestión Veterinaria

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange.svg)](https://developer.android.com/studio/releases/platforms)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-red.svg)](https://developer.android.com/studio/releases/platforms)

> Sistema móvil completo para la gestión de atenciones veterinarias con autenticación de usuarios, desarrollado con Kotlin y Jetpack Compose siguiendo las mejores prácticas de desarrollo Android y arquitectura limpia.

---

## Características Principales

### Sistema de Autenticación
- **Login seguro** con validación de credenciales
- **Recuperación de contraseña** mediante usuario + email
- **Gestión de sesión** con cierre automático al salir
- **Usuarios simulados** sin persistencia (memoria)
- **Contraseñas temporales** generadas aleatoriamente

### Acceso de Usuarios
- **Pantalla "Mi Información"** con datos del usuario autenticado
- **Estadísticas personalizadas** (consultas totales, mascotas atendidas)
- **Historial de consultas** del sistema
- **Información de perfil** (nombre, rol, email, usuario)

### Gestión de Atenciones
- **Registro completo de atenciones** (Dueño, Mascota, Consulta, Medicamento)
- **Validación en tiempo real** de formularios (email, teléfono, rangos)
- **Cálculo automático de descuentos** en medicamentos
- **Resumen dinámico** del sistema (Total mascotas, consultas, último dueño)
- **Historial de atenciones** con toda la información detallada
- **Compartir consultas** mediante Intent Implícito

### Componentes Avanzados
- **Content Provider** para compartir datos con otras apps
- **Broadcast Receiver** para detectar cambios de WiFi
- **Foreground Service** para notificaciones de recordatorios
- **Navigation Drawer** con menú global

### Interfaz de Usuario
- **Material Design 3** con tema personalizado corporativo (Azul Rey + Celeste)
- **Animaciones Fade In/Out** en todas las transiciones de pantalla
- **Splash Screen** animado con gradient y logo
- **Menú desplegable** (DropdownMenu) accesible desde todas las pantallas
- **Progress Indicators** (Circular y Linear) para feedback visual
- **Diseño responsive** adaptable a diferentes tamaños de pantalla

---

## Usuarios de Prueba

El sistema incluye 4 usuarios predefinidos para testing:

| Usuario | Contraseña | Email | Rol |
|---------|-----------|-------|-----|
| **admin** | admin123 | admin@veterinaria.cl | Administrador del Sistema |
| **veterinario** | vet123 | vet@veterinaria.cl | Veterinario Principal |
| **asistente** | asist123 | asist@veterinaria.cl | Asistente Veterinario |
| **cliente** | cliente123 | cliente@veterinaria.cl | Cliente |

### Recuperación de Contraseña

Para recuperar la contraseña:
1. Click en "¿Olvidaste tu contraseña?" en la pantalla de login
2. Ingresa el **usuario** y **email** correspondiente
3. El sistema generará una contraseña temporal (ej: `temp1234`)
4. Usa la nueva contraseña para iniciar sesión

**Ejemplo:**
- Usuario: `admin`
- Email: `admin@veterinaria.cl`
- Nueva contraseña: `temp5678` (generada automáticamente)

---

## Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Kotlin** | 1.9.20 | Lenguaje principal |
| **Jetpack Compose** | 2023.10.01 | UI declarativa moderna |
| **Material 3** | Latest | Componentes de diseño |
| **ViewModel** | 2.6.2 | Gestión de estado |
| **LiveData** | 2.6.2 | Observables reactivos |
| **Coroutines** | 1.7.3 | Programación asíncrona |
| **Kotlin Reflection** | 1.9.20 | Anotaciones runtime |
| **Android Gradle Plugin** | 8.2.0 | Build system |
| **Min SDK** | 21 (Android 5.0) | Compatibilidad mínima |
| **Target SDK** | 34 (Android 14) | Versión objetivo |

---

## Arquitectura

El proyecto sigue **múltiples patrones arquitectónicos** con separación clara de responsabilidades:

### Arquitectura MVVM + Clean Architecture

```
┌─────────────────────────────────────────────┐
│         VIEW LAYER (UI)                     │
│  - Jetpack Compose Screens                 │
│  - Material Design 3 Components            │
│  - Animations & Transitions                 │
│  - LoginScreen, AccesoUsuariosScreen        │
└─────────────┬───────────────────────────────┘
              │ observes (LiveData)
              ▼
┌─────────────────────────────────────────────┐
│         VIEWMODEL LAYER                     │
│  - AuthViewModel (Autenticación)            │
│  - MainViewModel (Estadísticas)             │
│  - RegistroViewModel (Registro)             │
│  - ConsultaViewModel (Historial)            │
│  - State Management (LiveData)              │
└─────────────┬───────────────────────────────┘
              │ uses
              ▼
┌─────────────────────────────────────────────┐
│         DOMAIN LAYER                        │
│  - Use Cases                                │
│  - Business Logic                           │
│  - VeterinariaService                       │
└─────────────┬───────────────────────────────┘
              │ uses
              ▼
┌─────────────────────────────────────────────┐
│         DATA LAYER                          │
│  - Repositories (AtencionRepository)        │
│  - Data Sources (In-Memory)                 │
│  - Models (Cliente, Mascota, Usuario)      │
│  - Content Provider                         │
│  - Broadcast Receiver                       │
└─────────────────────────────────────────────┘
```

### Principios SOLID Aplicados

- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Código abierto para extensión, cerrado para modificación
- **L**iskov Substitution: Las abstracciones son intercambiables
- **I**nterface Segregation: Interfaces específicas y segregadas
- **D**ependency Inversion: Dependencia de abstracciones, no implementaciones

---

## Estructura del Proyecto

```
VeterinariaApp/
│
├── app/src/main/
│   ├── java/com/duoc/veterinaria/
│   │   │
│   │   ├── annotations/
│   │   │   └── Promocionable.kt          # Anotación personalizada
│   │   │
│   │   ├── app/
│   │   │   └── VeterinariaApp.kt         # Navegación principal
│   │   │
│   │   ├── data/
│   │   │   ├── model/
│   │   │   │   ├── Cliente.kt
│   │   │   │   ├── Mascota.kt
│   │   │   │   ├── Consulta.kt
│   │   │   │   ├── Medicamento.kt
│   │   │   │   ├── RegistroAtencion.kt
│   │   │   │   ├── Veterinario.kt
│   │   │   │   └── Usuario.kt            
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── AtencionRepository.kt
│   │   │   │   ├── ClienteRepository.kt
│   │   │   │   └── RepositoryProvider.kt
│   │   │   │
│   │   │   └── service/
│   │   │       ├── VeterinariaService.kt
│   │   │       └── RecordatorioService.kt
│   │   │
│   │   ├── provider/
│   │   │   └── VeterinariaProvider.kt    # Content Provider
│   │   │
│   │   ├── receiver/
│   │   │   └── WifiReceiver.kt           # Broadcast Receiver
│   │   │
│   │   ├── ui/
│   │   │   ├── VeterinariaScreens.kt
│   │   │   ├── LoginScreen.kt            
│   │   │   ├── AccesoUsuariosScreen.kt   
│   │   │   ├── ServicioScreen.kt
│   │   │   ├── ProviderScreen.kt
│   │   │   ├── BroadcastTestScreen.kt
│   │   │   │
│   │   │   ├── navigation/
│   │   │   │   ├── AppScreen.kt          
│   │   │   │   └── VeterinariaTopBar.kt  
│   │   │   │
│   │   │   └── theme/
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   │
│   │   ├── utils/
│   │   │   └── Validaciones.kt
│   │   │
│   │   ├── viewmodel/
│   │   │   ├── AuthViewModel.kt          
│   │   │   ├── MainViewModel.kt
│   │   │   ├── RegistroViewModel.kt
│   │   │   └── ConsultaViewModel.kt
│   │   │
│   │   └── MainActivity.kt
│   │
│   ├── res/
│   │   ├── drawable/
│   │   │   ├── logo1.png
│   │   │   ├── logo2.png
│   │   │   ├── fondo_home.jpg
│   │   │   └── ...
│   │   │
│   │   └── values/
│   │       ├── colors.xml
│   │       ├── strings.xml
│   │       └── themes.xml
│   │
│   └── AndroidManifest.xml
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## Instalación

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17 o superior
- Android SDK 34
- Emulador o dispositivo físico con Android 5.0+

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/veterinaria-app.git
cd veterinaria-app
```

2. **Abrir en Android Studio**
```
File → Open → Seleccionar carpeta del proyecto
```

3. **Sync Gradle**
```
El IDE automáticamente sincronizará las dependencias
O manualmente: File → Sync Project with Gradle Files
```

4. **Configurar Emulador**
```
Tools → Device Manager → Create Device
Recomendado: Pixel 4 con API 33 o superior
```

5. **Ejecutar la aplicación**
```
Click en Run o Shift + F10
Seleccionar dispositivo y esperar la instalación
```

---

## Guía de Uso

### Iniciar Sesión
1. La app inicia con un **Splash Screen** animado (2 segundos)
2. Luego muestra la **pantalla de Login**
3. Ingresa credenciales (ejemplo: `admin` / `admin123`)
4. Click en "Iniciar Sesión"

### Recuperar Contraseña
1. Click en "¿Olvidaste tu contraseña?"
2. Ingresa usuario y email registrados
3. El sistema genera una contraseña temporal
4. Usa la nueva contraseña para entrar

### Navegar en la App
- **Menú lateral (⋮)**: Acceso a todas las funcionalidades
- **Mi Información**: Ver perfil y estadísticas personales
- **Registrar Atención**: Proceso paso a paso (4 pasos)
- **Ver Historial**: Consultas completas con opción de compartir
- **Gestión de Servicios**: Control del servicio de notificaciones
- **Content Provider**: Consultar datos compartidos
- **Broadcast Test**: Probar receptor WiFi

### Registrar una Atención
**Paso 1:** Datos del Dueño (nombre, email, teléfono)  
**Paso 2:** Datos de la Mascota (nombre, especie, edad, peso)  
**Paso 3:** Tipo de Consulta (General, Urgencia, Vacunación, Control)  
**Paso 4:** Medicamento/Vacuna (solo para Urgencia y Vacunación)

### Cerrar Sesión
- Desde cualquier pantalla: Menú (⋮) → "Cerrar Sesión"
- Volverás automáticamente al Login

---

## Componentes Técnicos Detallados

### AuthViewModel
```kotlin
class AuthViewModel : ViewModel() {
    // Estado de autenticación
    val isAuthenticated: LiveData<Boolean>
    val currentUser: LiveData<Usuario?>
    val errorMessage: LiveData<String?>
    
    // Funciones principales
    fun login(username: String, password: String, onSuccess: () -> Unit)
    fun resetPassword(username: String, email: String, onSuccess: (String) -> Unit)
    fun logout()
}
```

### AccesoUsuariosScreen
Muestra información del usuario autenticado:
- Datos personales (nombre, usuario, rol, email)
- Estadísticas del sistema (consultas totales, mascotas atendidas)
- Historial de las últimas 5 consultas
- Información adicional del sistema

### Navegación Protegida
```kotlin
Splash (2s) → Login → Home (autenticado)
                ↓
         Recuperar Contraseña
```

---

## Diseño y UX

### Paleta de Colores
- **Azul Rey** (`#2962FF`): Botones y elementos principales
- **Celeste Claro** (`#E3F2FD`): Fondo general
- **Celeste Oscuro** (`#90CAF9`): Contenedores y tarjetas

### Animaciones
- **Fade In/Out**: Transiciones entre pantallas (1000ms)
- **Circular Progress**: Indicadores de carga
- **Linear Progress**: Barra de progreso en formularios

### Accesibilidad
- Contraste adecuado en todos los textos
- Iconos descriptivos con `contentDescription`
- Tamaños de fuente legibles (12sp - 28sp)
- Botones con altura mínima de 56dp

---

## Licencia

Este proyecto fue desarrollado con fines educativos para **Duoc UC**.

```
Copyright © 2025 Duoc UC
Todos los derechos reservados
```

---

## Autor

**Leonardo Olivares**  
Estudiante de Desarrollo de Aplicaciones Móviles  
Duoc UC - 2024

---
