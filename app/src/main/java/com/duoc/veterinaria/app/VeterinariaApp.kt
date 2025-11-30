package com.duoc.veterinaria

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duoc.veterinaria.model.RegistroAtencion
import com.duoc.veterinaria.service.VeterinariaService

// Fíjate que NO hay una "class VeterinariaApp", la función está directa
@Composable
fun VeterinariaApp() {
    var currentScreen by remember { mutableStateOf("welcome") }
    var registros by remember { mutableStateOf(listOf<RegistroAtencion>()) }

    // Instanciamos el servicio una sola vez
    val service = remember { VeterinariaService() }

    when (currentScreen) {
        "welcome" -> WelcomeScreen(
            onStartClick = { currentScreen = "registro" }
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