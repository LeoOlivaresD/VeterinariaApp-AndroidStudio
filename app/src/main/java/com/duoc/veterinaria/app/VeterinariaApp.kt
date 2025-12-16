package com.duoc.veterinaria.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.ui.*
import com.duoc.veterinaria.ui.navigation.AppScreen
import com.duoc.veterinaria.viewmodel.ConsultaViewModel
import com.duoc.veterinaria.viewmodel.MainViewModel
import com.duoc.veterinaria.viewmodel.RegistroViewModel

/**
 * Composable principal de la aplicación.
 * Implementa el patrón MVVM con separación clara de responsabilidades.
 *
 * Principio SRP: Solo maneja la navegación entre pantallas.
 * Principio OCP: Fácil agregar nuevas pantallas sin modificar código existente.
 */
@Composable
fun VeterinariaApp(onExit: () -> Unit) {

    // Estado de navegación
    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    // ViewModels específicos por pantalla (Principio SRP)
    val mainViewModel: MainViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()
    val consultaViewModel: ConsultaViewModel = viewModel()

    // Observar estados desde ViewModels
    val totalMascotas by mainViewModel.totalMascotas.observeAsState(0)
    val totalConsultas by mainViewModel.totalConsultas.observeAsState(0)
    val ultimoDueno by mainViewModel.ultimoDueno.observeAsState("N/A")
    val registros by consultaViewModel.registros.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {

        // --- SPLASH SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Splash,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            SplashScreen(
                onSplashFinished = {
                    currentScreen = AppScreen.Welcome
                    mainViewModel.cargarEstadisticas() // Cargar datos al iniciar
                }
            )
        }

        // --- WELCOME SCREEN (HOME) ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Welcome,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            WelcomeScreen(
                totalMascotas = totalMascotas,
                totalConsultas = totalConsultas,
                ultimoDueno = ultimoDueno,
                onStartClick = { currentScreen = AppScreen.Registro },
                onVerRegistrosClick = {
                    consultaViewModel.cargarRegistros() // Recargar antes de mostrar
                    currentScreen = AppScreen.Resumen
                },
                onFinalizarApp = onExit,
                onNavigateTo = { dest ->
                    if (dest == AppScreen.Resumen) {
                        consultaViewModel.cargarRegistros()
                    }
                    currentScreen = dest
                }
            )
        }

        // --- REGISTRO SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Registro,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            RegistroScreen(
                viewModel = registroViewModel,
                onRegistroComplete = {
                    // Recargar estadísticas y consultas después de guardar
                    mainViewModel.cargarEstadisticas()
                    consultaViewModel.cargarRegistros()
                    currentScreen = AppScreen.Resumen
                },
                onBackClick = { currentScreen = AppScreen.Welcome },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }

        // --- RESUMEN SCREEN (HISTORIAL) ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Resumen,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            ResumenScreen(
                registros = registros,
                onNuevaAtencion = { currentScreen = AppScreen.Registro },
                onVolverInicio = {
                    mainViewModel.cargarEstadisticas() // Actualizar stats al volver
                    currentScreen = AppScreen.Welcome
                },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = onExit
            )
        }
    }
}