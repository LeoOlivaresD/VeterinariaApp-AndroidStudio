package com.duoc.veterinaria.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Definimos el esquema de colores Claros (Light Theme)
private val LightColorScheme = lightColorScheme(
    primary = AzulRey,              // Botones y Barras en Azul Rey
    onPrimary = Blanco,             // Texto sobre botones en Blanco
    primaryContainer = CelesteOscuro, // Contenedores suaves
    onPrimaryContainer = Negro,
    secondary = AzulRey,            // Acentos
    background = CelesteClaro,      // Fondo general Celeste
    surface = Blanco,               // Tarjetas blancas
    onBackground = Negro,
    onSurface = Negro
)

// (Opcional) Esquema Oscuro
private val DarkColorScheme = darkColorScheme(
    primary = AzulRey,
    secondary = CelesteOscuro,
    background = Negro
)

@Composable
fun VeterinariaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // IMPORTANTE: Ponemos esto en false para forzar nuestros colores azules
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Pintamos la barra de estado del color Azul Rey
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}