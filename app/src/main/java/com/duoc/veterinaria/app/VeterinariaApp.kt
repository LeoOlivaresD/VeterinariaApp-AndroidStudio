package com.duoc.veterinaria

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duoc.veterinaria.model.RegistroAtencion
import com.duoc.veterinaria.service.VeterinariaService

@Composable
fun VeterinariaApp(onExit: () -> Unit) { // 1. Recibimos el onExit
    var currentScreen by remember { mutableStateOf("welcome") }
    var registros by remember { mutableStateOf(listOf<RegistroAtencion>()) }

    val service = remember { VeterinariaService() }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onStartClick = { currentScreen = "registro" },
            onVerRegistrosClick = { currentScreen = "resumen" }
        )
        "registro" -> RegistroScreen(
            service = service,
            onRegistroComplete = { registro ->
                registros = registros + registro
                currentScreen = "resumen"
            },
            onBackClick = { currentScreen = "welcome" }
        )
        "resumen" -> ResumenScreen(
            registros = registros,
            onNuevaAtencion = { currentScreen = "registro" },
            onVolverInicio = { currentScreen = "welcome" },
            onFinalizarApp = onExit // 2. Se lo pasamos a la pantalla final
        )
    }
}