package com.duoc.veterinaria.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VeterinariaTopBar(
    titulo: String,
    onNavigateTo: (AppScreen) -> Unit,
    onExit: () -> Unit
) {
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
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones")
            }

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
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    }
                )

                // Opción 2: Registrar
                DropdownMenuItem(
                    text = { Text("Registrar Atención") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Registro)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                )

                // Opción 3: Historial
                DropdownMenuItem(
                    text = { Text("Ver Historial") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Resumen)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null
                        )
                    }
                )

                Divider()

                // Opción 4: Servicios
                DropdownMenuItem(
                    text = { Text("Gestión de Servicios") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Servicio)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null
                        )
                    }
                )

                // NUEVO: Opción 5: Content Provider
                DropdownMenuItem(
                    text = { Text("Content Provider") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Provider)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null
                        )
                    }
                )

                Divider()

                // Opción 6: Salir
                DropdownMenuItem(
                    text = { Text("Salir", color = Color.Red) },
                    onClick = {
                        menuExpanded = false
                        onExit()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    )
}