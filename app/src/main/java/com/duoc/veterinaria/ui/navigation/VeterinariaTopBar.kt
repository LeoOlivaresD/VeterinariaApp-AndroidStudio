package com.duoc.veterinaria.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.ui.theme.ThemeManager
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.TextDecrease
import androidx.compose.material.icons.filled.TextIncrease

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

            // Botón disminuir fuente
            IconButton(onClick = { com.duoc.veterinaria.ui.theme.FontSizeManager.decreaseFontSize() }) {
                Icon(
                    imageVector = Icons.Default.TextDecrease,
                    contentDescription = "Disminuir tamaño de texto"
                )
            }

            // Botón aumentar fuente
            IconButton(onClick = { com.duoc.veterinaria.ui.theme.FontSizeManager.increaseFontSize() }) {
                Icon(
                    imageVector = Icons.Default.TextIncrease,
                    contentDescription = "Aumentar tamaño de texto"
                )
            }
            // Botón de tema (modo oscuro/claro)
            IconButton(onClick = { ThemeManager.toggleTheme() }) {
                Icon(
                    imageVector = if (ThemeManager.isDarkMode)
                        Icons.Default.LightMode
                    else
                        Icons.Default.DarkMode,
                    contentDescription = if (ThemeManager.isDarkMode)
                        "Cambiar a modo claro"
                    else
                        "Cambiar a modo oscuro"
                )
            }

            // Botón de menú
            IconButton(onClick = { menuExpanded = true }) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones")
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Inicio") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Welcome)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Mi Información") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.AccesoUsuarios)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Gestión de Clientes") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.GestionClientes)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Demostración Persistencia") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.DemostracionPersistencia)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.List, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Comparación Rendimiento") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.ComparacionRendimiento)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Assessment, contentDescription = null)
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text("Registrar Atención") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Registro)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Ver Historial") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Resumen)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.List, contentDescription = null)
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text("Gestión de Servicios") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Servicio)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Content Provider") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.Provider)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }
                )

                DropdownMenuItem(
                    text = { Text("Broadcast Receiver Test") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.BroadcastTest)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Análisis de Rendimiento") },
                    onClick = {
                        menuExpanded = false
                        onNavigateTo(AppScreen.AnalisisRendimiento)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Assessment, contentDescription = null)
                    }
                )

                Divider()

                DropdownMenuItem(
                    text = { Text("Cerrar Sesión", color = Color.Red) },
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