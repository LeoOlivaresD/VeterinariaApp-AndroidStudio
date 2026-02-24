package com.duoc.veterinaria

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.veterinaria.ui.GestionClientesScreen
import com.duoc.veterinaria.viewmodel.ClientesViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de UI para la pantalla de Gestión de Clientes.
 *
 * Estos tests verifican que los elementos principales de la interfaz
 * se renderizan correctamente usando Compose UI Testing con Espresso.
 *
 * ESTRATEGIA DE TESTING:
 * - Usar assertExists() en lugar de assertIsDisplayed() para mayor tolerancia
 * - Verificar elementos que siempre están presentes (título, campos de entrada)
 * - No depender de elementos que puedan estar fuera del scroll inicial
 */
@RunWith(AndroidJUnit4::class)
class GestionClientesUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test 1: Verificar que el título de la pantalla se renderiza correctamente.
     *
     * Este es el test más básico - verifica que la pantalla principal se carga.
     */
    @Test
    fun tituloPantalla_seRenderizaCorrectamente() {
        // Arrange: Montar la pantalla
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que el título está presente
        composeTestRule
            .onNodeWithText("Gestión de Clientes")
            .assertExists()
    }

    /**
     * Test 2: Verificar que los campos de entrada están presentes.
     *
     * Verifica que el formulario de registro se renderiza con sus campos básicos.
     */
    @Test
    fun camposDeEntrada_estanPresentes() {
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar campos del formulario
        composeTestRule
            .onNodeWithText("Nombre del cliente")
            .assertExists()

        composeTestRule
            .onNodeWithText("Email")
            .assertExists()

        composeTestRule
            .onNodeWithText("Teléfono")
            .assertExists()
    }

    /**
     * Test 3: Verificar que el botón de guardar existe en la pantalla.
     *
     * Usa assertExists() que es menos estricto que assertIsDisplayed().
     */
    @Test
    fun botonGuardar_existe() {
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que el botón existe (puede estar fuera de vista)
        composeTestRule
            .onNodeWithText("Guardar Cliente")
            .assertExists()
    }

    /**
     * Test 4: Verificar que la funcionalidad de búsqueda está presente.
     *
     * Comprueba que existe el campo de búsqueda en la interfaz.
     */
    @Test
    fun campoBusqueda_existe() {
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que existe el campo de búsqueda
        composeTestRule
            .onNodeWithText("Buscar cliente")
            .assertExists()
    }

    /**
     * Test 5: Verificar que la sección de clientes registrados está presente.
     *
     * Comprueba que el título de la lista de clientes se renderiza.
     */
    @Test
    fun seccionClientesRegistrados_existe() {
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que el título de la sección existe
        composeTestRule
            .onNodeWithText("Clientes Registrados (Room Database)", substring = true)
            .assertExists()
    }
}