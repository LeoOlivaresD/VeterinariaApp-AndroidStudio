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
 */
@RunWith(AndroidJUnit4::class)
class GestionClientesUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Test 1: Verificar que la pantalla principal se renderiza
     * y que el botón "Guardar" está visible para el usuario.
     */
    @Test
    fun botonGuardar_estaVisible() {
        // Arrange: Montar la pantalla
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que el botón Guardar está presente
        composeTestRule
            .onNodeWithText("Guardar")
            .assertIsDisplayed()
    }

    /**
     * Test 2: Verificar navegación y elementos de la pantalla.
     * Comprueba que el campo de búsqueda existe en la interfaz.
     */
    @Test
    fun campoBusqueda_existe() {
        composeTestRule.setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val app = context.applicationContext as android.app.Application
            val viewModel = ClientesViewModel(app)

            GestionClientesScreen(clientesViewModel = viewModel)
        }

        // Assert: Verificar que existe funcionalidad de búsqueda
        composeTestRule
            .onNodeWithText("Buscar", substring = true, ignoreCase = true)
            .assertExists()
    }
}