package com.duoc.veterinaria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.veterinaria.utils.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AnalisisRendimientoScreen() {
    val TAG = AppLogger.Tags.PERFORMANCE

    var isProcessing by remember { mutableStateOf(false) }
    var resultado by remember { mutableStateOf("") }
    var tiempoEjecucion by remember { mutableStateOf(0L) }
    var memoriaUsada by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        AppLogger.i(TAG, "AnalisisRendimientoScreen iniciada")
        onDispose {
            AppLogger.i(TAG, "AnalisisRendimientoScreen destruida")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Análisis de Rendimiento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Esta pantalla simula operaciones pesadas para demostrar el uso de herramientas de profiling y análisis de rendimiento.",
            style = MaterialTheme.typography.bodyMedium
        )

        Divider()

        // Card informativa
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Herramientas de Android Studio",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "1. Android Profiler: View > Tool Windows > Profiler",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "2. CPU Profiler: Analiza uso de CPU por método",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "3. Memory Profiler: Detecta memory leaks",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    "4. Logcat: Logs con medición de tiempo",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Operación 1: Procesamiento intensivo de CPU
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Operación 1: Procesamiento CPU",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Simula cálculos intensivos en Dispatchers.Default",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        scope.launch {
                            AppLogger.i(TAG, "Iniciando operación CPU intensiva")
                            isProcessing = true
                            resultado = ""

                            val startTime = System.currentTimeMillis()
                            val memoriaInicial = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()

                            withContext(Dispatchers.Default) {
                                AppLogger.d(TAG, "Ejecutando en Dispatchers.Default")

                                // Simular cálculo intensivo
                                var sum = 0L
                                repeat(10_000_000) {
                                    sum += it
                                }

                                delay(2000)
                                resultado = "Cálculo completado: suma = $sum"
                            }

                            val endTime = System.currentTimeMillis()
                            tiempoEjecucion = endTime - startTime

                            val memoriaFinal = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
                            memoriaUsada = "${(memoriaFinal - memoriaInicial) / 1024 / 1024} MB"

                            AppLogger.performance(TAG, "Operación CPU intensiva", tiempoEjecucion)
                            AppLogger.i(TAG, "Memoria usada: $memoriaUsada")

                            isProcessing = false
                        }
                    },
                    enabled = !isProcessing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ejecutar procesamiento CPU")
                }
            }
        }

        // Operación 2: Operación I/O simulada
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Operación 2: I/O Simulado",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Simula operaciones de base de datos en Dispatchers.IO",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        scope.launch {
                            AppLogger.i(TAG, "Iniciando operación I/O")
                            isProcessing = true
                            resultado = ""

                            val startTime = System.currentTimeMillis()

                            withContext(Dispatchers.IO) {
                                AppLogger.d(TAG, "Ejecutando en Dispatchers.IO")

                                // Simular lectura de base de datos
                                repeat(5) {
                                    delay(500)
                                    AppLogger.d(TAG, "Leyendo registro ${it + 1}/5")
                                }

                                resultado = "5 registros leídos exitosamente"
                            }

                            val endTime = System.currentTimeMillis()
                            tiempoEjecucion = endTime - startTime

                            AppLogger.performance(TAG, "Operación I/O", tiempoEjecucion)

                            isProcessing = false
                        }
                    },
                    enabled = !isProcessing,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ejecutar operación I/O")
                }
            }
        }

        // Indicador de procesamiento
        if (isProcessing) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Procesando... Revisa Logcat y Profiler")
                }
            }
        }

        // Resultados
        if (resultado.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Resultados",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Resultado: $resultado")
                    Text("Tiempo ejecución: ${tiempoEjecucion}ms")
                    if (memoriaUsada.isNotEmpty()) {
                        Text("Memoria adicional: $memoriaUsada")
                    }
                }
            }
        }

        // Instrucciones
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Cómo usar Android Profiler",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "1. Abre: View > Tool Windows > Profiler\n" +
                            "2. Selecciona tu app en ejecución\n" +
                            "3. Click en CPU, Memory o Network\n" +
                            "4. Ejecuta las operaciones de esta pantalla\n" +
                            "5. Observa los picos de uso en tiempo real\n" +
                            "6. Toma capturas de pantalla para el informe",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}