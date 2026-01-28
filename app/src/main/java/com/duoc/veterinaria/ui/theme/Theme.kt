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

// Esquema de colores CLARO
private val LightColorScheme = lightColorScheme(
    primary = AzulRey,
    onPrimary = Blanco,
    primaryContainer = CelesteOscuro,
    onPrimaryContainer = Negro,
    secondary = AzulRey,
    onSecondary = Blanco,
    secondaryContainer = CelesteClaro,
    onSecondaryContainer = Negro,
    background = CelesteClaro,
    onBackground = Negro,
    surface = Blanco,
    onSurface = Negro,
    surfaceVariant = CelesteOscuro,
    onSurfaceVariant = Negro,
    error = androidx.compose.ui.graphics.Color(0xFFB00020),
    onError = Blanco
)

// Esquema de colores OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = CelesteOscuro,
    onPrimary = Negro,
    primaryContainer = AzulRey,
    onPrimaryContainer = Blanco,
    secondary = CelesteOscuro,
    onSecondary = Negro,
    secondaryContainer = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
    onSecondaryContainer = Blanco,
    background = androidx.compose.ui.graphics.Color(0xFF121212),
    onBackground = Blanco,
    surface = androidx.compose.ui.graphics.Color(0xFF1E1E1E),
    onSurface = Blanco,
    surfaceVariant = androidx.compose.ui.graphics.Color(0xFF2C2C2C),
    onSurfaceVariant = Blanco,
    error = androidx.compose.ui.graphics.Color(0xFFCF6679),
    onError = Negro
)

@Composable
fun VeterinariaAppTheme(
    darkTheme: Boolean = ThemeManager.isDarkMode,
    dynamicColor: Boolean = true,
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
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}