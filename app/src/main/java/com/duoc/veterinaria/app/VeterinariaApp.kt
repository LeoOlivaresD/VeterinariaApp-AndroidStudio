package com.duoc.veterinaria.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.ui.RegistroScreen
import com.duoc.veterinaria.ui.ResumenScreen
import com.duoc.veterinaria.ui.WelcomeScreen
import com.duoc.veterinaria.viewmodel.VeterinariaViewModel

@Composable
fun VeterinariaApp(onExit: () -> Unit) {
    var currentScreen by remember { mutableStateOf("welcome") }

    // 1. Instanciamos el ViewModel (Sobrevive a rotaciones y cambios de configuración)
    val viewModel: VeterinariaViewModel = viewModel()

    // 2. Observamos los datos del ViewModel como Estado de Compose
    val registros by viewModel.registros.observeAsState(initial = emptyList())

    // Nota: El servicio ya está dentro del ViewModel, no necesitamos instanciarlo aquí

    // Cálculos para el resumen (Usando los datos observados)
    val totalMascotas = registros.map { it.mascota.nombre }.distinct().size
    val totalConsultas = registros.size
    val ultimoDueno = registros.lastOrNull()?.dueno?.nombre ?: "N/A"

    Box(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(
            visible = currentScreen == "welcome",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            WelcomeScreen(
                onStartClick = { currentScreen = "registro" },
                onVerRegistrosClick = { currentScreen = "resumen" },
                onFinalizarApp = onExit,
                totalMascotas = totalMascotas,
                totalConsultas = totalConsultas,
                ultimoDueno = ultimoDueno,
                onNavigateTo = { dest -> currentScreen = dest }
            )
        }

        AnimatedVisibility(
            visible = currentScreen == "registro",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            // Pasamos el servicio que está dentro del ViewModel para que RegistroScreen pueda leer las listas
            // (Idealmente refactorizaríamos RegistroScreen para recibir el ViewModel,
            // pero para no romper tu código actual, podemos pasar el servicio interno o exponer funciones)

            /* NOTA IMPORTANTE: Como tu RegistroScreen actual espera un 'VeterinariaService' y
               una función 'onRegistroComplete', adaptaremos la llamada para usar el ViewModel.
            */

            // Creamos una instancia temporal del servicio solo para pasarla a la UI antigua
            // (En una refactorización completa, la UI debería pedir datos al ViewModel, no al Service)
            val tempService = com.duoc.veterinaria.data.service.VeterinariaService()

            RegistroScreen(
                service = tempService,
                onRegistroComplete = { nuevoRegistro ->
                    // AQUI ESTA LA MAGIA: Usamos el ViewModel para guardar
                    viewModel.agregarRegistro(nuevoRegistro) {
                        // Cuando termine de guardar (coroutine), navegamos
                        currentScreen = "resumen"
                    }
                },
                onBackClick = { currentScreen = "welcome" },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }

        AnimatedVisibility(
            visible = currentScreen == "resumen",
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ResumenScreen(
                registros = registros, // Pasamos la lista observada del ViewModel
                onNuevaAtencion = { currentScreen = "registro" },
                onVolverInicio = { currentScreen = "welcome" },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }
    }
}