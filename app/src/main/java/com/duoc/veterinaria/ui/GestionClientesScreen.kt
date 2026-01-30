package com.duoc.veterinaria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.data.model.Cliente
import com.duoc.veterinaria.utils.AppLogger
import com.duoc.veterinaria.viewmodel.ClientesViewModel
import com.duoc.veterinaria.viewmodel.ClientesViewModelFactory

@Composable
fun GestionClientesScreen(
    clientesViewModel: ClientesViewModel = viewModel(
        factory = ClientesViewModelFactory(
            LocalContext.current.applicationContext as android.app.Application
        )
    )
) {
    val TAG = AppLogger.Tags.UI

    DisposableEffect(Unit) {
        AppLogger.i(TAG, "GestionClientesScreen compuesta")
        onDispose {
            AppLogger.i(TAG, "GestionClientesScreen destruida")
        }
    }

    val clientesFiltrados by clientesViewModel.clientesFiltrados.observeAsState(emptyList())
    val isLoading by clientesViewModel.isLoading.observeAsState(false)
    val isSearching by clientesViewModel.isSearching.observeAsState(false)
    val searchQuery by clientesViewModel.searchQuery.observeAsState("")
    val errorState by clientesViewModel.errorState.observeAsState()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(clientesViewModel.obtenerUltimoEmail()) }
    var telefono by remember { mutableStateOf("") }
    var mensajeUi by remember { mutableStateOf("") }
    var mensajeTipo by remember { mutableStateOf("info") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Gestión de Clientes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Los datos se almacenan localmente usando Room, SQLite y SharedPreferences",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Divider()

        // Mostrar errores del ViewModel
        if (errorState != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Error detectado",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = errorState ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    IconButton(onClick = {
                        clientesViewModel.clearError()
                        AppLogger.d(TAG, "Error limpiado por el usuario")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Cerrar",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        // Formulario de registro
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                AppLogger.d(TAG, "Campo nombre modificado")
            },
            label = { Text("Nombre del cliente") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = errorState?.contains("nombre", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                AppLogger.d(TAG, "Campo email modificado")
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = errorState?.contains("email", ignoreCase = true) == true
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = {
                telefono = it
                AppLogger.d(TAG, "Campo teléfono modificado")
            },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = errorState?.contains("teléfono", ignoreCase = true) == true
        )

        Button(
            onClick = {
                AppLogger.i(TAG, "Usuario presionó botón Guardar Cliente")

                if (nombre.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty()) {
                    AppLogger.d(TAG, "Validación de campos OK, iniciando registro")
                    clientesViewModel.registrarCliente(nombre, email, telefono)
                    mensajeUi = "Cliente registrado correctamente en base de datos local"
                    mensajeTipo = "success"
                    nombre = ""
                    email = ""
                    telefono = ""
                } else {
                    AppLogger.w(TAG, "Validación fallida: campos vacíos")
                    mensajeUi = "Por favor completa todos los campos"
                    mensajeTipo = "warning"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (isLoading) "Guardando..." else "Guardar Cliente")
        }

        if (mensajeUi.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when (mensajeTipo) {
                        "success" -> MaterialTheme.colorScheme.primaryContainer
                        "warning" -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.secondaryContainer
                    }
                )
            ) {
                Text(
                    text = mensajeUi,
                    modifier = Modifier.padding(12.dp),
                    color = when (mensajeTipo) {
                        "success" -> MaterialTheme.colorScheme.onPrimaryContainer
                        "warning" -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            }
        }

        Divider()

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                AppLogger.d(TAG, "Búsqueda iniciada con query: $it")
                clientesViewModel.buscarClientes(it)
            },
            label = { Text("Buscar cliente") },
            placeholder = { Text("Nombre, email o teléfono") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        AppLogger.d(TAG, "Búsqueda limpiada por el usuario")
                        clientesViewModel.limpiarBusqueda()
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Indicador de búsqueda
        if (isSearching) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Buscando...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Clientes Registrados (Room Database)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (searchQuery.isNotEmpty()) {
                Text(
                    "${clientesFiltrados.size} resultados",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (clientesFiltrados.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (searchQuery.isNotEmpty())
                        "No se encontraron clientes con '$searchQuery'"
                    else
                        "No hay clientes registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(clientesFiltrados) { cliente: Cliente ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                cliente.nombre,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                "Email: ${cliente.email}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "Teléfono: ${cliente.telefono}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}