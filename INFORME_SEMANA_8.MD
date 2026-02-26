# Informe Técnico - Semana 8
## Validación, Publicación y Generación de APK

---

## Introducción

Este informe documenta las actividades realizadas durante la **Semana 8**, enfocadas en la validación final del proyecto, resolución de problemas técnicos, y generación del APK firmado para distribución. El trabajo se centró en:

- Resolución de errores de compilación en modo Release
- Limpieza de recursos corruptos
- Configuración de firma digital del APK
- Generación de APK firmado con keystore personal
- Preparación final del proyecto para entrega

---

## Contexto Inicial

Al iniciar la Semana 8, el proyecto ya contaba con:

- Arquitectura MVVM completamente implementada
- Componentes Jetpack integrados (ViewModel, LiveData, Room, Navigation)
- 20 tests funcionando (15 unitarios + 5 UI)
- Base de datos local con persistencia
- Todas las funcionalidades operativas en modo Debug

**Desafío:** Generar el APK firmado para distribución cumpliendo con los estándares de publicación Android.

---

## Problemas Encontrados y Soluciones

### Problema 1: Error de Compilación de Recursos en Modo Release

#### Descripción del Error

Al intentar generar el APK firmado, el proceso de compilación fallaba con el siguiente error:

```
Execution failed for task ':app:mergeReleaseResources'.
Android resource compilation failed
ERROR: C:\Users\Leonardo\...\res\drawable\logo1.png: AAPT: error: file failed to compile.
```

#### Análisis

- El proyecto compilaba correctamente en modo **Debug**
- Fallaba únicamente en modo **Release**
- El archivo `logo1.png` estaba corrupto o mal formateado
- AAPT (Android Asset Packaging Tool) es más estricto en Release que en Debug

**Diferencia Debug vs Release:**

| Aspecto | Debug | Release |
|---------|-------|---------|
| Validación de recursos | Permisiva | Estricta |
| Optimización | Mínima | Máxima |
| Compilación de imágenes | Tolerante | Rigurosa |
| Permite archivos corruptos | Sí | No |

#### Solución Implementada

**Paso 1:** Identificación del archivo problemático
```
drawable/logo1.png
```

**Paso 2:** Eliminación del recurso corrupto
```
1. Navegación a: app/src/main/res/drawable/
2. Eliminación de logo1.png
3. Eliminación de logo2.png (también presentaba problemas)
```

**Paso 3:** Creación de logo limpio
- Se generó un nuevo logo optimizado: `logo_veterinaria.png`
- Formato: PNG válido, sin metadatos corruptos
- Tamaño: 512x512px (estándar Android)
- Optimizado para compilación Release

**Paso 4:** Actualización de referencias
```kotlin
// Buscar y reemplazar en código
R.drawable.logo1 → R.drawable.logo_veterinaria
```

**Resultado:**
```
✓ Proyecto compila sin errores en modo Release
✓ Todos los recursos validados correctamente
```

---

### Problema 2: Error de Recursos del Ícono Launcher

#### Descripción del Error

Después de resolver el problema del logo, apareció un segundo error:

```
Android resource linking failed
error: resource drawable/icon_app_background not found.
com.duoc.veterinaria.app-main-68:/mipmap-anydpi-v26/icon_app.xml:3
```

#### Análisis

- Los archivos XML del ícono adaptativo hacían referencia a recursos faltantes
- `icon_app_background` no existía en `drawable/`
- Los archivos `icon_app.xml` y `icon_app_round.xml` estaban mal configurados

#### Solución Implementada

**Paso 1:** Eliminación de archivos corruptos
```
app/src/main/res/mipmap-anydpi-v26/
├── icon_app.xml        → ELIMINADO
└── icon_app_round.xml  → ELIMINADO
```

**Paso 2:** Actualización de AndroidManifest.xml
```xml
<!-- ANTES -->
<application
    android:icon="@mipmap/icon_app"
    android:roundIcon="@mipmap/icon_app_round"

<!-- DESPUÉS -->
<application
    android:icon="@mipmap/ic_launcher"
    android:roundIcon="@mipmap/ic_launcher_round"
```

**Paso 3:** Limpieza del proyecto
```
Build → Clean Project
Build → Rebuild Project
```

**Resultado:**
```
✓ AndroidManifest.xml corregido
✓ Referencias a íconos válidas
✓ Proyecto compila sin errores
```

---

## Generación del APK Firmado

### Proceso de Firma Digital

Una vez resueltos los errores de compilación, se procedió a generar el APK firmado siguiendo el estándar de Android.

### Paso 1: Creación del Keystore

**Herramienta utilizada:** Android Studio - Generate Signed Bundle or APK

**Configuración del Keystore:**
```
Key store path: C:\Users\Leonardo\veterinaria-release-key.jks
Password: ****************
Alias: veterinaria-key
Validity: 25 años
```

**Información del Certificado:**
```
First and Last Name: Leonardo Olivares
Organizational Unit: Desarrollo Mobile
Organization: Duoc UC
City: Santiago
State: Región Metropolitana
Country Code: CL
```


### Paso 2: Generación del APK

**Comando ejecutado internamente:**
```bash
# Android Studio ejecuta automáticamente
./gradlew assembleRelease
```

---

## Características del APK Generado

### Información Técnica

```
Nombre: app-release.apk
Versión: 1.0 (versionCode: 1)
Tamaño: 5.2 MB
Package Name: com.duoc.veterinaria
Min SDK: 21 (Android 5.0)
Target SDK: 34 (Android 14)
Firmado: Sí (veterinaria-key)
Optimizado: Sí (ProGuard/R8)
```


## Gestión del Keystore

### Importancia del Keystore

El keystore es **crítico** para:
- Actualizar la aplicación en el futuro
- Mantener la identidad de la app
- Publicar en Google Play Store
- Probar la autenticidad del desarrollador

**Consecuencias de perder el keystore:**
-  No se puede actualizar la app
-  Necesidad de publicar como app nueva
-  Pérdida de usuarios e instalaciones
-  Pérdida de reviews y calificaciones


## Instrucciones de Instalación del APK

### En Emulador Android Studio

**Método 1: Drag and Drop**
```
1. Abrir emulador desde Device Manager
2. Arrastrar app-release.apk a la pantalla
3. Esperar instalación automática
4. Abrir app desde launcher
```

**Método 2: ADB**
```bash
adb install app/release/app-release.apk
```

### En Dispositivo Físico

**Prerequisitos:**
```
1. Habilitar "Orígenes desconocidos" en Configuración
2. Habilitar "Instalación de apps desconocidas" para el navegador/gestor de archivos
```

**Proceso:**
```
1. Transferir app-release.apk al dispositivo (USB/email/Drive)
2. Abrir el archivo APK desde el gestor de archivos
3. Confirmar instalación
4. Aceptar permisos solicitados
5. Abrir app desde launcher
```

---

**Fecha de Elaboración:** 25 de Febrero de 2026
**Alumno:** Leonardo Olivares
**Asignatura:** Desarrollo de Aplicaciones Móviles II
**Institución:** Duoc UC
**Semana:** 8 - Validación y Publicación de Aplicaciones Android