package com.duoc.veterinaria.ui

import android.database.Cursor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.veterinaria.data.local.prefs.ClientesPrefs
import com.duoc.veterinaria.data.local.sqlite.ClientesLogDbHelper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import kotlinx.coroutines.launch

@Composable
fun DemostracionPersistenciaScreen() {
    val context = LocalContext.current
    val prefs = remember { ClientesPrefs(context) }
    val logsHelper = remember { ClientesLogDbHelper(context) }

    var ultimoEmail by remember { mutableStateOf("") }
    var ultimoGuardado by remember { mutableStateOf("") }
    var logsTexto by remember { mutableStateOf("") }
    var mensajeActualizacion by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        ultimoEmail = prefs.getUltimoEmail()
        val millis = prefs.getUltimoGuardadoMillis()
        ultimoGuardado = if (millis > 0) {
            java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
                .format(java.util.Date(millis))
        } else {
            "Sin datos"
        }

        logsTexto = obtenerLogs(logsHelper)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Demostración de Persistencia",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Esta pantalla muestra las 3 tecnologías de persistencia funcionando:",
            style = MaterialTheme.typography.bodyMedium
        )

        // Room
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "1. ROOM DATABASE",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Base de datos SQLite con ORM.\n" +
                            "Almacena los clientes de forma estructurada.\n" +
                            "Ver datos en: Gestión de Clientes",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // SharedPreferences
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "2. SHARED PREFERENCES",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Almacenamiento clave-valor ligero.",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Último email guardado:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    ultimoEmail.ifEmpty { "Sin datos" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Último guardado:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    ultimoGuardado,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // SQLite directo
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "3. SQLITE DIRECTO",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "SQLiteOpenHelper tradicional.\n" +
                            "Registra logs de eventos:",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        logsTexto.ifEmpty { "Sin logs registrados" },
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // Boton actualizar
        Button(
            onClick = {
                ultimoEmail = prefs.getUltimoEmail()
                val millis = prefs.getUltimoGuardadoMillis()
                ultimoGuardado = if (millis > 0) {
                    java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
                        .format(java.util.Date(millis))
                } else {
                    "Sin datos"
                }
                logsTexto = obtenerLogs(logsHelper)
                mensajeActualizacion = "Datos actualizados correctamente"

                // Scroll automático hacia abajo
                coroutineScope.launch {
                    kotlinx.coroutines.delay(100)
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Datos")
        }

        // Mensaje con animación
        AnimatedVisibility(
            visible = mensajeActualizacion.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = mensajeActualizacion,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

private fun obtenerLogs(helper: ClientesLogDbHelper): String {
    return try {
        val db = helper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT evento, fechaMillis FROM clientes_log ORDER BY id DESC LIMIT 10",
            null
        )

        val resultado = StringBuilder()
        var contador = 1

        if (cursor.moveToFirst()) {
            do {
                val evento = cursor.getString(0)
                val fecha = cursor.getLong(1)
                val fechaFormato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date(fecha))
                resultado.append("$contador. $evento\n   $fechaFormato\n\n")
                contador++
            } while (cursor.moveToNext())
        }

        cursor.close()
        resultado.toString().ifEmpty { "Sin logs" }
    } catch (e: Exception) {
        "Error: ${e.message}"
    }
}