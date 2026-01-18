# 🐾 Veterinaria App - Sistema de Gestión Veterinaria

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4.svg)](https://developer.android.com/jetpack/compose)
[![Room](https://img.shields.io/badge/Database-Room-00C853.svg)](https://developer.android.com/training/data-storage/room)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-21-orange.svg)](https://developer.android.com/studio/releases/platforms)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34-red.svg)](https://developer.android.com/studio/releases/platforms)

> Sistema móvil completo para la gestión de atenciones veterinarias con autenticación de usuarios y persistencia de datos local, desarrollado con Kotlin y Jetpack Compose siguiendo las mejores prácticas de desarrollo Android y arquitectura limpia.

---

## Características Principales

### Sistema de Autenticación
- **Login seguro** con validación de credenciales
- **Recuperación de contraseña** mediante usuario + email
- **Gestión de sesión** con cierre automático al salir
- **Usuarios simulados** sin persistencia (memoria)
- **Contraseñas temporales** generadas aleatoriamente

### Persistencia de Datos Local
- **Room Database**: Almacenamiento estructurado de clientes con SQLite + ORM
- **SharedPreferences**: Gestión de preferencias de usuario y metadatos
- **SQLite Directo**: Registro de logs y auditoría de eventos
- **Funcionamiento offline**: Todos los datos persisten sin necesidad de conexión
- **Sincronización automática**: Los datos se actualizan en tiempo real

### Gestión de Clientes
- **Registro de clientes** con validación de datos
- **Persistencia permanente** en base de datos local (Room)
- **Lista observable** que se actualiza automáticamente
- **Pre-llenado inteligente** del último email usado
- **Logs de auditoría** de todas las operaciones
- **Pantalla de demostración** que muestra las 3 tecnologías funcionando

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
- **Scroll automático** con feedback visual en actualizaciones

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
| **Room** | 2.6.1 | Base de datos SQLite con ORM |
| **SQLite** | Native | Base de datos directa para logs |
| **SharedPreferences** | Native | Almacenamiento clave-valor |
| **ViewModel** | 2.6.2 | Gestión de estado |
| **LiveData** | 2.6.2 | Observables reactivos |
| **Flow** | Latest | Streams reactivos de datos |
| **Coroutines** | 1.7.3 | Programación asíncrona |
| **KSP** | 1.9.20-1.0.14 | Procesamiento de anotaciones |
| **Kotlin Reflection** | 1.9.20 | Anotaciones runtime |
| **Android Gradle Plugin** | 8.2.0 | Build system |
| **Min SDK** | 21 (Android 5.0) | Compatibilidad mínima |
| **Target SDK** | 34 (Android 14) | Versión objetivo |

---

## Arquitectura

El proyecto sigue **múltiples patrones arquitectónicos** con separación clara de responsabilidades:

### Arquitectura MVVM + Clean Architecture + Repository Pattern
```
┌─────────────────────────────────────────────┐
│         VIEW LAYER (UI)                     │
│  - Jetpack Compose Screens                 │
│  - Material Design 3 Components            │
│  - Animations & Transitions                 │
│  - LoginScreen, GestionClientesScreen       │
│  - DemostracionPersistenciaScreen           │
└─────────────┬───────────────────────────────┘
              │ observes (LiveData/Flow)
              ▼
┌─────────────────────────────────────────────┐
│         VIEWMODEL LAYER                     │
│  - AuthViewModel (Autenticación)            │
│  - ClientesViewModel (Gestión Clientes)     │
│  - MainViewModel (Estadísticas)             │
│  - RegistroViewModel (Registro)             │
│  - ConsultaViewModel (Historial)            │
│  - State Management (LiveData/StateFlow)    │
└─────────────┬───────────────────────────────┘
              │ uses
              ▼
┌─────────────────────────────────────────────┐
│         REPOSITORY LAYER                    │
│  - ClientePersistenciaRepository            │
│  - AtencionRepository                       │
│  - Coordina múltiples fuentes de datos     │
└─────────────┬───────────────────────────────┘
              │ uses
              ▼
┌─────────────────────────────────────────────┐
│         DATA SOURCES                        │
│  - Room (ClienteDao + VeterinariaDatabase)  │
│  - SharedPreferences (ClientesPrefs)        │
│  - SQLite (ClientesLogDbHelper)             │
│  - In-Memory (otras entidades)              │
└─────────────────────────────────────────────┘
```

### Capas de Persistencia
```
┌─────────────────────────────────────────────┐
│    CAPA 1: ROOM DATABASE                    │
│  - Base de datos estructurada con ORM       │
│  - Entidades: ClienteEntity                 │
│  - DAOs: ClienteDao                         │
│  - Database: VeterinariaDatabase            │
│  - Uso: Datos estructurados permanentes     │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│    CAPA 2: SHARED PREFERENCES               │
│  - Almacenamiento clave-valor ligero        │
│  - Clase: ClientesPrefs                     │
│  - Uso: Preferencias de usuario, metadatos │
│  - Ejemplo: Último email usado              │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│    CAPA 3: SQLITE DIRECTO                   │
│  - SQLiteOpenHelper tradicional             │
│  - Clase: ClientesLogDbHelper               │
│  - Uso: Logs de auditoría, eventos         │
│  - Ejemplo: Registro de inserciones        │
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
│   │   │   └── Promocionable.kt          
│   │   │
│   │   ├── app/
│   │   │   └── VeterinariaApp.kt         
│   │   │
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── dao/
│   │   │   │   │   └── ClienteDao.kt     # DAO de Room
│   │   │   │   ├── db/
│   │   │   │   │   └── VeterinariaDatabase.kt  # Database Room
│   │   │   │   ├── entity/
│   │   │   │   │   └── ClienteEntity.kt  # Entidad Room
│   │   │   │   ├── prefs/
│   │   │   │   │   └── ClientesPrefs.kt  # SharedPreferences
│   │   │   │   └── sqlite/
│   │   │   │       └── ClientesLogDbHelper.kt  # SQLite directo
│   │   │   │
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
│   │   │   │   ├── ClientePersistenciaRepository.kt  # Repo de persistencia
│   │   │   │   └── RepositoryProvider.kt
│   │   │   │
│   │   │   └── service/
│   │   │       ├── VeterinariaService.kt
│   │   │       └── RecordatorioService.kt
│   │   │
│   │   ├── provider/
│   │   │   └── VeterinariaProvider.kt    
│   │   │
│   │   ├── receiver/
│   │   │   └── WifiReceiver.kt           
│   │   │
│   │   ├── ui/
│   │   │   ├── VeterinariaScreens.kt
│   │   │   ├── LoginScreen.kt            
│   │   │   ├── AccesoUsuariosScreen.kt
│   │   │   ├── GestionClientesScreen.kt  # Pantalla de clientes
│   │   │   ├── DemostracionPersistenciaScreen.kt  # Demo 3 tecnologías
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
│   │   │   ├── ClientesViewModel.kt      # ViewModel de clientes
│   │   │   ├── ClientesViewModelFactory.kt
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
- **Gestión de Clientes**: Registrar y ver clientes (persistencia local)
- **Demostración Persistencia**: Ver las 3 tecnologías funcionando
- **Registrar Atención**: Proceso paso a paso (4 pasos)
- **Ver Historial**: Consultas completas con opción de compartir
- **Gestión de Servicios**: Control del servicio de notificaciones
- **Content Provider**: Consultar datos compartidos
- **Broadcast Test**: Probar receptor WiFi

### Gestionar Clientes (Persistencia Local)

#### Registrar un Cliente
1. Ve al menú (⋮) y selecciona "Gestión de Clientes"
2. Completa los campos:
    - Nombre del cliente
    - Email (se guarda automáticamente para uso futuro)
    - Teléfono
3. Presiona "Guardar Cliente"
4. El cliente se almacena permanentemente en la base de datos local
5. Aparece en la lista de "Clientes Registrados"

#### Verificar Persistencia
1. Registra uno o más clientes
2. Cierra completamente la aplicación
3. Vuelve a abrir la app
4. Los clientes siguen apareciendo en la lista

#### Ver Demostración de las 3 Tecnologías
1. Ve al menú (⋮) y selecciona "Demostración Persistencia"
2. Observa las 3 secciones:
    - **Room Database**: Almacenamiento estructurado
    - **SharedPreferences**: Último email y timestamp
    - **SQLite Directo**: Logs de eventos
3. Presiona "Actualizar Datos" para refrescar la información
4. La pantalla hace scroll automático y muestra feedback visual

### Registrar una Atención
**Paso 1:** Datos del Dueño (nombre, email, teléfono)  
**Paso 2:** Datos de la Mascota (nombre, especie, edad, peso)  
**Paso 3:** Tipo de Consulta (General, Urgencia, Vacunación, Control)  
**Paso 4:** Medicamento/Vacuna (solo para Urgencia y Vacunación)

### Cerrar Sesión
- Desde cualquier pantalla: Menú (⋮) → "Cerrar Sesión"
- Volverás automáticamente al Login
- Los datos locales persisten después de cerrar sesión

---

## Componentes Técnicos Detallados

### ClientesViewModel
```kotlin
class ClientesViewModel(app: Application) : AndroidViewModel(app) {
    // Lista observable de clientes desde Room
    val clientes: LiveData<List<Cliente>>
    
    // Funciones principales
    fun registrarCliente(nombre: String, email: String, telefono: String)
    fun obtenerUltimoEmail(): String
}
```

### ClientePersistenciaRepository
```kotlin
class ClientePersistenciaRepository(
    private val dao: ClienteDao,
    private val prefs: ClientesPrefs,
    private val logs: ClientesLogDbHelper
) {
    // Flow observable de clientes
    fun clientesFlow(): Flow<List<ClienteEntity>>
    
    // Registro con las 3 tecnologías
    suspend fun registrarCliente(nombre: String, email: String, telefono: String): Boolean
}
```

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
- **Expand/Shrink**: Mensajes de confirmación
- **Circular Progress**: Indicadores de carga
- **Linear Progress**: Barra de progreso en formularios
- **Scroll Automático**: Navegación fluida a contenido nuevo

### Accesibilidad
- Contraste adecuado en todos los textos
- Iconos descriptivos con `contentDescription`
- Tamaños de fuente legibles (12sp - 28sp)
- Botones con altura mínima de 56dp
- Feedback visual en todas las interacciones

---

## Pruebas de Persistencia

### Verificar Room Database
1. Registra 3 clientes diferentes
2. Cierra la app completamente (Force Stop)
3. Vuelve a abrir la app
4. Verifica que los 3 clientes siguen en la lista

### Verificar SharedPreferences
1. Registra un cliente con email `test@ejemplo.com`
2. Cierra la app
3. Vuelve a abrir y ve a "Gestión de Clientes"
4. El campo email debe mostrar `test@ejemplo.com`

### Verificar SQLite Logs
1. Ve a "Demostración Persistencia"
2. Registra 2 clientes nuevos
3. Vuelve a "Demostración Persistencia"
4. Presiona "Actualizar Datos"
5. Verifica que aparecen nuevos logs con las inserciones

---

## Licencia

Este proyecto fue desarrollado con fines educativos para **Duoc UC**.
```
Copyright © 2026 Duoc UC
Todos los derechos reservados
```

---

## Autor

**Leonardo Olivares**  
Estudiante de Desarrollo de Aplicaciones  
Duoc UC

---