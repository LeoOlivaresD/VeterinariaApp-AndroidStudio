package com.duoc.veterinaria.app
import com.duoc.veterinaria.ui.GestionClientesScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.ui.*
import com.duoc.veterinaria.ui.navigation.AppScreen
import com.duoc.veterinaria.viewmodel.AuthViewModel
import com.duoc.veterinaria.viewmodel.ConsultaViewModel
import com.duoc.veterinaria.viewmodel.MainViewModel
import com.duoc.veterinaria.viewmodel.RegistroViewModel
import com.duoc.veterinaria.ui.HomeVeterinariaScreen
import com.duoc.veterinaria.ui.DemostracionPersistenciaScreen
@Composable
fun VeterinariaApp(onExit: () -> Unit) {

    var currentScreen by remember { mutableStateOf(AppScreen.Splash) }

    // ViewModels
    val authViewModel: AuthViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()
    val consultaViewModel: ConsultaViewModel = viewModel()

    // Estados observados
    val isAuthenticated by authViewModel.isAuthenticated.observeAsState(false)
    val currentUser by authViewModel.currentUser.observeAsState()
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
                    currentScreen = AppScreen.Login
                }
            )
        }

        // --- LOGIN SCREEN  ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Login,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    currentScreen = AppScreen.Welcome
                    mainViewModel.cargarEstadisticas()
                }
            )
        }
    //PANTALLA ANTIGUA
        /** --- WELCOME SCREEN (HOME) ---
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
                    consultaViewModel.cargarRegistros()
                    currentScreen = AppScreen.Resumen
                },
                onFinalizarApp = {
                    authViewModel.logout()
                    currentScreen = AppScreen.Login
                },
                onNavigateTo = { dest ->
                    if (dest == AppScreen.Resumen) {
                        consultaViewModel.cargarRegistros()
                    }
                    currentScreen = dest
                }
            )
        }
**/
        //PANTALLA NUEVA PARA ACTIVIDAD SEMANA 1
// --- WELCOME SCREEN (HOME) ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Welcome,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Veterinaria Duoc UC",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    HomeVeterinariaScreen(
                        totalMascotas = totalMascotas,
                        totalConsultas = totalConsultas,
                        ultimoDueno = ultimoDueno,
                        onNavigateToRegistro = { currentScreen = AppScreen.Registro },
                        onNavigateToHistorial = {
                            consultaViewModel.cargarRegistros()
                            currentScreen = AppScreen.Resumen
                        }
                    )
                }
            }
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
                    mainViewModel.cargarEstadisticas()
                    consultaViewModel.cargarRegistros()
                    currentScreen = AppScreen.Resumen
                },
                onBackClick = { currentScreen = AppScreen.Welcome },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = {
                    authViewModel.logout()
                    currentScreen = AppScreen.Login
                }
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
                    mainViewModel.cargarEstadisticas()
                    currentScreen = AppScreen.Welcome
                },
                onNavigateTo = { dest -> currentScreen = dest },
                onExit = {
                    authViewModel.logout()
                    currentScreen = AppScreen.Login
                }
            )
        }

        // --- SERVICIO SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Servicio,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Gestión de Servicios",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ServicioScreen()
                }
            }
        }

        // --- PROVIDER SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.Provider,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Content Provider",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ProviderScreen()
                }
            }
        }

        // --- BROADCAST TEST SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.BroadcastTest,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Broadcast Receiver Test",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    BroadcastTestScreen()
                }
            }
        }

        // --- ACCESO USUARIOS SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.AccesoUsuarios,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Mi Información",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    AccesoUsuariosScreen(
                        currentUser = currentUser,
                        registros = registros
                    )
                }
            }
        }

        // --- GESTION CLIENTES SCREEN ---  // AGREGA AQUI
        AnimatedVisibility(
            visible = currentScreen == AppScreen.GestionClientes,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Gestión de Clientes",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    GestionClientesScreen()
                }
            }
        }
        // --- DEMOSTRACION PERSISTENCIA SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.DemostracionPersistencia,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Demostración Persistencia",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    DemostracionPersistenciaScreen()
                }
            }

        }
        // --- COMPARACION RENDIMIENTO SCREEN ---
        AnimatedVisibility(
            visible = currentScreen == AppScreen.ComparacionRendimiento,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(1000))
        ) {
            Scaffold(
                topBar = {
                    com.duoc.veterinaria.ui.navigation.VeterinariaTopBar(
                        "Comparación Rendimiento",
                        { dest -> currentScreen = dest },
                        {
                            authViewModel.logout()
                            currentScreen = AppScreen.Login
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    ComparacionRendimientoScreen()
                }
            }
        }
    }
}