package com.duoc.veterinaria.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.ui.navigation.AppScreen // Importamos el Enum que acabamos de revisar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaTopBar(
    titulo: String,
    onNavigateTo: (AppScreen) -> Unit, // Recibe la acción de navegar usando el Enum
    onExit: () -> Unit                 // Recibe la acción de cerrar la app
) {
    // Estado para controlar si el menú de 3 puntos está abierto o cerrado
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(text = titulo, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            // Icono de 3 puntos (MoreVert)
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menú de opciones")
            }

            // Menú Desplegable
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                // Opción 1: Inicio
                DropdownMenuItem(
                    text = { Text("Inicio") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Welcome)
                    }
                )
                // Opción 2: Registrar
                DropdownMenuItem(
                    text = { Text("Registrar Atención") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Registro)
                    }
                )
                // Opción 3: Historial
                DropdownMenuItem(
                    text = { Text("Ver Historial") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Resumen)
                    }
                )

                Divider() // Línea divisoria visual

                // Opción 4: Salir (en rojo)
                DropdownMenuItem(
                    text = { Text("Salir", color = Color.Red) },
                    onClick = {
                        menuExpanded = false
                        onExit()
                    }
                )
            }
        }
    )
}