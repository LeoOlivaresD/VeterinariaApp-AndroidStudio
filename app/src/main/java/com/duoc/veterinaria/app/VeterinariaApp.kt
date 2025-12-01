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
import com.duoc.veterinaria.ui.navigation.AppScreen // Importamos el Enum
import com.duoc.veterinaria.ui.RegistroScreen
import com.duoc.veterinaria.ui.ResumenScreen
import com.duoc.veterinaria.ui.SplashScreen
import com.duoc.veterinaria.ui.WelcomeScreen
import com.duoc.veterinaria.viewmodel.VeterinariaViewModel

@Composable
fun VeterinariaApp(onExit: () -> Unit) {
    // 1. Estado inicial usando AppScreen
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    val viewModel: VeterinariaViewModel = viewModel()
    val registros by viewModel.registros.observeAsState(initial = emptyList())

    val totalMascotas = registros.map { it.mascota.nombre }.distinct().size
    val totalConsultas = registros.size
    val ultimoDueno = registros.lastOrNull()?.dueno?.nombre ?: "N/A"

    Box(modifier = Modifier.fillMaxSize()) {

        // PANTALLA SPLASH
        //Transiciones suaves al iniciar y cambiar de pantalla usando AnimatedVisibility
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Splash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SplashScreen(
                onSplashFinished = {
                    // Al terminar, vamos al Home
                    currentScreen = AppScreen.Welcome
                }
            )
        }

        // PANTALLA HOME (WELCOME)
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Welcome,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            WelcomeScreen(
                // AQUÍ ARREGLAMOS LOS BOTONES:
                onStartClick = { currentScreen = AppScreen.Registro }, // Botón Registrar
                onVerRegistrosClick = { currentScreen = AppScreen.Resumen }, // Botón Ver Historial
                onFinalizarApp = onExit, // Botón Salir

                totalMascotas = totalMascotas,
                totalConsultas = totalConsultas,
                ultimoDueno = ultimoDueno,

                // Navegación desde el menú
                onNavigateTo = { dest -> currentScreen = dest }
            )
        }

        // PANTALLA REGISTRO
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Registro,
            enter = fadeIn(), // Animación de entrada fadeIn
            exit = fadeOut() // Animación de salida fadeOut
        ) {
            val tempService = com.duoc.veterinaria.data.service.VeterinariaService()

            RegistroScreen(
                service = tempService,
                onRegistroComplete = { nuevoRegistro ->
                    viewModel.agregarRegistro(nuevoRegistro) {
                        currentScreen = AppScreen.Resumen // Al guardar, vamos a Resumen
                    }
                },
                onBackClick = { currentScreen = AppScreen.Welcome },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }

        // PANTALLA RESUMEN
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Resumen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ResumenScreen(
                registros = registros,
                onNuevaAtencion = { currentScreen = AppScreen.Registro },
                onVolverInicio = { currentScreen = AppScreen.Welcome },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }
    }
}