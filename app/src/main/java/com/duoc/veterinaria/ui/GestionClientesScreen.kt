package com.duoc.veterinaria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.veterinaria.data.model.Cliente
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
    val clientesFiltrados by clientesViewModel.clientesFiltrados.observeAsState(emptyList())
    val isLoading by clientesViewModel.isLoading.observeAsState(false)
    val isSearching by clientesViewModel.isSearching.observeAsState(false)
    val searchQuery by clientesViewModel.searchQuery.observeAsState("")

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(clientesViewModel.obtenerUltimoEmail()) }
    var telefono by remember { mutableStateOf("") }
    var mensajeUi by remember { mutableStateOf("") }

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

        // Formulario de registro
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del cliente") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Button(
            onClick = {
                if (nombre.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty()) {
                    clientesViewModel.registrarCliente(nombre, email, telefono)
                    mensajeUi = "Cliente registrado correctamente en base de datos local"
                    nombre = ""
                    email = ""
                    telefono = ""
                } else {
                    mensajeUi = "Por favor completa todos los campos"
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = mensajeUi,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Divider()

        // Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { clientesViewModel.buscarClientes(it) },
            label = { Text("Buscar cliente") },
            placeholder = { Text("Nombre, email o teléfono") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { clientesViewModel.limpiarBusqueda() }) {
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