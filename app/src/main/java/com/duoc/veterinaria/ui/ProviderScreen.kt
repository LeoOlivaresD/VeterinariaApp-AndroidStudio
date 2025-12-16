package com.duoc.veterinaria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.provider.VeterinariaProvider

@Composable
fun ProviderScreen() {
    val context = LocalContext.current
    var resultado by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Content Provider",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prueba el Content Provider para compartir datos con otras aplicaciones",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón: Consultar Mascotas
        Button(
            onClick = {
                cargando = true
                resultado = consultarProvider(context, VeterinariaProvider.MASCOTAS_URI, "Mascotas")
                cargando = false
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text("📋 Consultar Mascotas")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón: Consultar Consultas
        Button(
            onClick = {
                cargando = true
                resultado = consultarProvider(context, VeterinariaProvider.CONSULTAS_URI, "Consultas")
                cargando = false
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text("📝 Consultar Historial")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón: Consultar Estadísticas
        Button(
            onClick = {
                cargando = true
                resultado = consultarProvider(context, VeterinariaProvider.ESTADISTICAS_URI, "Estadísticas")
                cargando = false
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando
        ) {
            Text("📊 Consultar Estadísticas")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (cargando) {
            CircularProgressIndicator()
        }

        if (resultado.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Resultado:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = resultado,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ℹ️ Información técnica",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        Authority: com.duoc.veterinaria.provider
                        
                        URIs disponibles:
                        • content://...provider/mascotas
                        • content://...provider/consultas
                        • content://...provider/estadisticas
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Función helper para consultar el Content Provider
 */
private fun consultarProvider(
    context: android.content.Context,
    uri: android.net.Uri,
    tipo: String
): String {
    return try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        if (cursor == null) {
            return "❌ Error: No se pudo obtener datos"
        }

        val resultado = StringBuilder()
        resultado.append("✅ $tipo obtenidos correctamente\n\n")
        resultado.append("Total de registros: ${cursor.count}\n")
        resultado.append("─".repeat(40) + "\n\n")

        if (cursor.moveToFirst()) {
            val columnNames = cursor.columnNames
            var contador = 1

            do {
                resultado.append("Registro #$contador:\n")
                columnNames.forEach { columnName ->
                    val index = cursor.getColumnIndex(columnName)
                    val value = cursor.getString(index)
                    resultado.append("  • $columnName: $value\n")
                }
                resultado.append("\n")
                contador++
            } while (cursor.moveToNext())
        }

        cursor.close()
        resultado.toString()

    } catch (e: Exception) {
        "❌ Error al consultar: ${e.message}"
    }
}