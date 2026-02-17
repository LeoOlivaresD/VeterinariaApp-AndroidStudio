package com.duoc.veterinaria.ui

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.veterinaria.R
import kotlinx.coroutines.delay

import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

// Modelo de datos para veterinarios
data class Veterinario(
    val nombre: String,
    val especialidad: String,
    val horario: String
)

@Composable
fun HomeVeterinariaScreen(
    totalMascotas: Int,
    totalConsultas: Int,
    ultimoDueno: String,
    onNavigateToRegistro: () -> Unit,
    onNavigateToHistorial: () -> Unit
) {
    val context = LocalContext.current

    // Animación fade-in de imagen
    var showImage by remember { mutableStateOf(false) }
    val alphaImage by animateFloatAsState(
        targetValue = if (showImage) 1f else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "alphaImage"
    )
    LaunchedEffect(Unit) { showImage = true }

    // Animación fade-out del mensaje
    var showMessage by remember { mutableStateOf(true) }
    val alphaMessage by animateFloatAsState(
        targetValue = if (showMessage) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alphaMessage"
    )
    LaunchedEffect(Unit) {
        delay(3000)
        showMessage = false
    }

    // Lista de veterinarios disponibles
    val veterinarios = remember {
        listOf(
            Veterinario("Dra. López Martínez", "Medicina General", "Lun–Vie 09:00–18:00"),
            Veterinario("Dr. González Pérez", "Cirugía Veterinaria", "Lun–Sáb 10:00–14:00"),
            Veterinario("Dra. Rodríguez Silva", "Vacunación y Control", "Lun–Vie 11:00–19:00"),
            Veterinario("Dr. Fernández Torres", "Emergencias 24h", "Turnos rotativos")
        )
    }

    // Estados UI
    var veterinarioSeleccionado by remember { mutableStateOf<Veterinario?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        // Imagen con animación usando Coil
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(R.drawable.fondo_home)
                .crossfade(true)
                .build(),
            contentDescription = "Bienvenida Veterinaria",
            modifier = Modifier
                .alpha(alphaImage)
                .fillMaxWidth()
                .height(180.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de bienvenida con fade-out
        if (alphaMessage > 0f) {
            Text(
                text = "Bienvenido al Sistema Veterinario Duoc UC",
                modifier = Modifier.alpha(alphaMessage),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Gestiona las consultas de tus mascotas de forma rápida y eficiente.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Card de resumen (jerarquía visual)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Resumen del Sistema",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem(label = "Mascotas atendidas", value = "$totalMascotas")
                    StatItem(label = "Consultas totales", value = "$totalConsultas")
                }
                Text(
                    "Último registro: $ultimoDueno",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción (accesibilidad: tamaño táctil adecuado)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onNavigateToRegistro,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Pets, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Registrar")
            }

            OutlinedButton(
                onClick = onNavigateToHistorial,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Assessment, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Historial")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Título de lista
        Text(
            text = "Veterinarios Disponibles",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn (componente avanzado 1: RecyclerView en Compose)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(veterinarios) { vet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Veterinario ${vet.nombre}, especialidad ${vet.especialidad}"
                        }
                        .clickable {
                            veterinarioSeleccionado = vet
                            mostrarDialogo = true
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = vet.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = vet.especialidad,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = "Horario: ${vet.horario}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
        }
    }

    // AlertDialog (componente avanzado 2: Dialog)
    if (mostrarDialogo && veterinarioSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmar selección") },
            text = {
                Text(
                    "¿Deseas agendar una consulta con:\n\n${veterinarioSeleccionado!!.nombre}\n(${veterinarioSeleccionado!!.especialidad})?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    // Toast (componente avanzado 3: retroalimentación)
                    Toast.makeText(
                        context,
                        "Consulta agendada con ${veterinarioSeleccionado!!.nombre}",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    mostrarDialogo = false
                    Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}