# 🐾 Veterinaria App - Sistema de Gestión Veterinaria

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange.svg)](https://developer.android.com/studio/releases/platforms)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-red.svg)](https://developer.android.com/studio/releases/platforms)

> Sistema móvil completo para la gestión de atenciones veterinarias, desarrollado con Kotlin y Jetpack Compose siguiendo las mejores prácticas de desarrollo Android.

## Características

### Interfaz de Usuario
- **Material Design 3** con tema personalizado corporativo (Azul Rey + Celeste)
- **Animaciones Fade In/Out** en todas las transiciones de pantalla
- **Splash Screen** animado con gradient y logo
- **Menú desplegable** (DropdownMenu) accesible desde todas las pantallas
- **Progress Indicators** (Circular y Linear) para feedback visual
- **Diseño responsive** adaptable a diferentes tamaños de pantalla

### Funcionalidades
- **Registro completo de atenciones** (Dueño, Mascota, Consulta, Medicamento)
- **Validación en tiempo real** de formularios (email, teléfono, rangos)
- **Cálculo automático de descuentos** en medicamentos
- **Resumen dinámico** del sistema (Total mascotas, consultas, último dueño)
- **Historial de atenciones** con toda la información detallada
- **Navegación fluida** entre módulos con animaciones

### Técnicas
- **ViewModel + LiveData** para gestión de estado reactiva
- **Kotlin Coroutines** para operaciones asíncronas
- **Reflection** para anotaciones personalizadas (`@Promocionable`)
- **Sobrecarga de operadores** para operaciones con medicamentos
- **Validaciones con Regex** para email y teléfono
- **Sistema de navegación con Enum** (type-safe)

---

## 🛠️ Tecnologías Utilizadas

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

## 🏗️ Arquitectura

El proyecto sigue el patrón **MVVM (Model-View-ViewModel)** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────────┐
│              VIEW (UI Layer)                 │
│  - Jetpack Compose Screens                  │
│  - Material Design Components                │
│  - Animations & Transitions                  │
└─────────────┬───────────────────────────────┘
              │ observes
              ▼
┌─────────────────────────────────────────────┐
│         VIEWMODEL (Presentation)             │
│  - VeterinariaViewModel                      │
│  - LiveData State Management                 │
│  - Business Logic Orchestration              │
└─────────────┬───────────────────────────────┘
              │ uses
              ▼
┌─────────────────────────────────────────────┐
│         MODEL (Data Layer)                   │
│  - Data Models (Cliente, Mascota, etc.)     │
│  - Service Layer (VeterinariaService)       │
│  - Utilities & Validations                   │
└─────────────────────────────────────────────┘
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

## Estructura del Proyecto

```
VeterinariaApp/
│
├── app/
│   ├── src/main/
│   │   ├── java/com/duoc/veterinaria/
│   │   │   ├── annotations/
│   │   │   │   └── Promocionable.kt          # Anotación personalizada
│   │   │   │
│   │   │   ├── app/
│   │   │   │   └── VeterinariaApp.kt         # Navegación principal
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── model/                     # Modelos de datos
│   │   │   │   │   ├── Cliente.kt
│   │   │   │   │   ├── Mascota.kt
│   │   │   │   │   ├── Consulta.kt
│   │   │   │   │   ├── Medicamento.kt
│   │   │   │   │   ├── RegistroAtencion.kt
│   │   │   │   │   ├── Veterinario.kt
│   │   │   │   │   └── ...
│   │   │   │   │
│   │   │   │   └── service/
│   │   │   │       └── VeterinariaService.kt # Lógica de negocio
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── VeterinariaScreens.kt     # Pantallas principales
│   │   │   │   │
│   │   │   │   ├── navigation/
│   │   │   │   │   ├── AppScreen.kt          # Enum de navegación
│   │   │   │   │   └── VeterinariaTopBar.kt  # TopBar con menú
│   │   │   │   │
│   │   │   │   └── theme/
│   │   │   │       ├── Color.kt              # Paleta de colores
│   │   │   │       ├── Theme.kt              # Tema Material 3
│   │   │   │       └── Type.kt               # Tipografía
│   │   │   │
│   │   │   ├── utils/
│   │   │   │   └── Validaciones.kt           # Validaciones y utilidades
│   │   │   │
│   │   │   ├── viewmodel/
│   │   │   │   └── VeterinariaViewModel.kt   # ViewModel principal
│   │   │   │
│   │   │   └── MainActivity.kt               # Punto de entrada
│   │   │
│   │   ├── res/
│   │   │   ├── drawable/                      # Imágenes y recursos
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── ...
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   └── build.gradle.kts                       # Dependencias del módulo
│
├── build.gradle.kts                           # Configuración del proyecto
├── settings.gradle.kts                        # Configuración de Gradle
├── gradle.properties                          # Properties de Gradle
└── README.md                                  # Este archivo
```

---

## 🚀 Funcionalidades Principales

### 1. Splash Screen Animado
- Pantalla de carga inicial con gradient y logo
- Animación Fade In al iniciar
- Duración: 2 segundos
- Transición automática a pantalla Welcome

### 2. Pantalla Welcome (Inicio)
**Características:**
- Resumen dinámico del sistema:
  - Total de mascotas atendidas
  - Total de consultas realizadas
  - Último dueño registrado
- Botones de acceso rápido:
  - Registrar Nueva Atención
  - Ver Consultas Registradas
- Menú desplegable en TopBar

### 3. Registro de Atención (4 Pasos)
**Paso 1: Datos del Dueño**
- Nombre (requerido)
- Email (validación con regex)
- Teléfono (formato chileno: 9XXXXXXXX)
- Validación en tiempo real con mensajes de error

**Paso 2: Datos de la Mascota**
- Nombre (requerido)
- Especie (Perro, Gato, etc.)
- Edad (rango 0-50 años)
- Peso (rango 0.1-200 kg)

**Paso 3: Tipo de Consulta**
- Consulta general ($15,000)
- Urgencia ($20,000)
- Vacunación ($10,000)
- Control ($12,000)

**Paso 4: Selección de Medicamento/Vacuna**
- Mostrado solo para Urgencia y Vacunación
- Cálculo automático de descuentos:
  - 20% descuento en periodo promocional (días 10-20)
  - Descuentos por anotación `@Promocionable`
- Precio final con descuento aplicado

**Indicadores de Progreso:**
- LinearProgressIndicator: Muestra "Paso X de 4"
- CircularProgressIndicator al guardar: "Guardando registro..."

### 4. Historial de Atenciones
- Lista completa de todas las atenciones registradas
- Información detallada de cada registro:
  - Fecha y hora
  - Datos del dueño
  - Información de la mascota
  - Tipo de consulta y costo
  - Medicamento (si aplica) con precio final
- Botones de acción:
  - Registrar otra atención
  - Volver al Inicio

### 5. Menú de Navegación Global
Accesible desde **todas las pantallas** mediante ícono ⋮:
- Inicio
- Registrar Atención
- Ver Historial
- Salir (en rojo)

### 6. Animaciones y Transiciones
- **Fade In:** Al mostrar nuevas pantallas
- **Fade Out:** Al ocultar pantallas actuales
- **Timing optimizado:** 1000ms para transiciones suaves
- **AnimatedVisibility:** Control declarativo de visibilidad

### 7. Notificaciones de Progreso
- **Contextuales:** Mensaje cambia según la acción
- **Overlay semitransparente:** Bloquea interacción durante carga
- **CircularProgressIndicator:** Indicador visual giratorio
- **Logo personalizado:** Incluye branding de la veterinaria
- **Mensajes:**
  - "Cargando módulos..."
  - "Guardando registro..."
  - "Cargando..."

---

## Licencia

Este proyecto fue desarrollado con fines educativos para Duoc UC.

```
Copyright © 2024 Duoc UC
Todos los derechos reservados
```

---

## Autor

**Leonardo Olivares **  
Estudiante de Desarrollo de Aplicaciones Móviles  
Duoc UC - 2024

---
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose)

</div>
