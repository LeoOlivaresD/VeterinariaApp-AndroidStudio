package com.duoc.veterinaria.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun ComparacionRendimientoScreen() {
    var resultadoSinCoroutines by remember { mutableStateOf("") }
    var resultadoConCoroutines by remember { mutableStateOf("") }
    var cargandoSinCoroutines by remember { mutableStateOf(false) }
    var cargandoConCoroutines by remember { mutableStateOf(false) }
    var uiCongelada by remember { mutableStateOf(false) }
    var ejecutarSinCoroutines by remember { mutableStateOf(false) }
    var ejecutarConCoroutines by remember { mutableStateOf(false) }

    // LaunchedEffect para operación SIN optimización (Composable scope)
    LaunchedEffect(ejecutarSinCoroutines) {
        if (ejecutarSinCoroutines) {
            cargandoSinCoroutines = true
            uiCongelada = true
            resultadoSinCoroutines = ""

            // Simular operación pesada EN EL HILO PRINCIPAL (MAL PRÁCTICA)
            withContext(Dispatchers.Main) {
                Thread.sleep(3000)
            }

            resultadoSinCoroutines = "Completado en 3 segundos - UI bloqueada durante proceso"
            cargandoSinCoroutines = false
            uiCongelada = false
            ejecutarSinCoroutines = false
        }
    }

    // LaunchedEffect para operación CON optimización (Dispatchers.IO)
    LaunchedEffect(ejecutarConCoroutines) {
        if (ejecutarConCoroutines) {
            cargandoConCoroutines = true
            resultadoConCoroutines = ""

            // Procesar en segundo plano con Dispatchers.IO (BUENA PRÁCTICA)
            val resultado = withContext(Dispatchers.IO) {
                delay(3000) // Simular operación pesada (BD, red, etc)
                "Completado en 3 segundos - UI permaneció responsiva"
            }

            resultadoConCoroutines = resultado
            cargandoConCoroutines = false
            ejecutarConCoroutines = false
        }
    }

    // LA UI SE CARGA EN EL HILO PRINCIPAL (Composición)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Comparación de Rendimiento",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Demostración del impacto de usar Coroutines con Dispatchers apropiados vs procesamiento bloqueante.",
            style = MaterialTheme.typography.bodyMedium
        )

        Divider()

        // SECCIÓN 1: SIN COROUTINES (ANTES - BLOQUEANTE)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "ANTES: Sin Optimización",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Text(
                    "Operación pesada ejecutada en Main Thread.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Text(
                    "Problema: UI se congela, usuario no puede interactuar.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                Button(
                    onClick = { ejecutarSinCoroutines = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    enabled = !cargandoSinCoroutines && !cargandoConCoroutines
                ) {
                    Text("Ejecutar SIN Coroutines (Bloqueante)")
                }

                if (cargandoSinCoroutines) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "UI CONGELADA",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                "No puedes hacer scroll ni interactuar",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                if (resultadoSinCoroutines.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            "Resultado: $resultadoSinCoroutines",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // SECCIÓN 2: CON COROUTINES (DESPUÉS - OPTIMIZADO)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "DESPUÉS: Con Optimización (Coroutines)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    "Operación pesada ejecutada en Dispatchers.IO.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    "Beneficio: UI permanece responsiva, usuario puede seguir interactuando.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = { ejecutarConCoroutines = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !cargandoConCoroutines && !cargandoSinCoroutines
                ) {
                    Text("Ejecutar CON Coroutines (No bloqueante)")
                }

                if (cargandoConCoroutines) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = Color(0xFF2E7D32)
                            )
                            Column {
                                Text(
                                    "Procesando en segundo plano",
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "Puedes hacer scroll e interactuar",
                                    color = Color(0xFF2E7D32),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                if (resultadoConCoroutines.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            "Resultado: $resultadoConCoroutines",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // SECCIÓN 3: Explicación técnica
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Explicación Técnica",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Scopes según contexto:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    """
                    • Activity: lifecycleScope
                    • Composable: LaunchedEffect (usado aquí)
                    • ViewModel: viewModelScope
                    • Manual: CoroutineScope(Dispatchers.IO)
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Dispatchers disponibles:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    """
                    • Dispatchers.Main: UI (composición, actualización)
                    • Dispatchers.IO: Base de datos, red, archivos
                    • Dispatchers.Default: Cálculos intensivos, filtros
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Patrón implementado:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    """
                    1. UI se carga en Main (inmediato)
                    2. Datos se cargan en IO (paralelo)
                    3. UI se actualiza cuando datos están listos
                    
                    Resultado: Experiencia fluida sin bloqueos
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Indicador visual del estado de la UI
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (uiCongelada)
                    Color(0xFFFF5252).copy(alpha = 0.2f)
                else
                    Color(0xFF4CAF50).copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    if (uiCongelada)
                        "Estado UI: BLOQUEADA (no interactiva)"
                    else
                        "Estado UI: ACTIVA (puedes hacer scroll)",
                    color = if (uiCongelada) Color(0xFFC62828) else Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}