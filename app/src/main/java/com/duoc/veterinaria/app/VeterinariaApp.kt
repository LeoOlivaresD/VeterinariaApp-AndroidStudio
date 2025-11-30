package com.duoc.veterinaria.app

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
            onFinalizarApp = onExit,

            // --- CÁLCULOS DINÁMICOS PARA LA SEMANA 5 ---
            // Contamos mascotas únicas por nombre
            totalMascotas = registros.map { it.mascota.nombre }.distinct().size,
            // Total de registros en la lista
            totalConsultas = registros.size,
            // Nombre del último dueño (o "N/A" si está vacío)
            ultimoDueno = registros.lastOrNull()?.dueno?.nombre ?: "N/A"
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