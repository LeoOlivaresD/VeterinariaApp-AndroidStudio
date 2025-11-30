package com.duoc.veterinaria

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duoc.veterinaria.data.model.RegistroAtencion
import com.duoc.veterinaria.data.service.VeterinariaService
import com.duoc.veterinaria.ui.RegistroScreen
import com.duoc.veterinaria.ui.ResumenScreen
import com.duoc.veterinaria.ui.WelcomeScreen

@Composable
fun VeterinariaApp(onExit: () -> Unit) { // 1. Recibimos el onExit
    var currentScreen by remember { mutableStateOf("welcome") }
    var registros by remember { mutableStateOf(listOf<RegistroAtencion>()) }

    val service = remember { VeterinariaService() }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onStartClick = { currentScreen = "registro" },
            onVerRegistrosClick = { currentScreen = "resumen" },
            onFinalizarApp = onExit // 3. Conectamos el botón al cierre de la app
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
            onVolverInicio = { currentScreen = "welcome" }
        )
    }
}