package com.duoc.veterinaria.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duoc.veterinaria.receiver.WifiReceiver

@Composable
fun BroadcastTestScreen() {
    val context = LocalContext.current
    var receiverRegistrado by remember { mutableStateOf(false) }
    val wifiReceiver = remember { WifiReceiver() }

    DisposableEffect(Unit) {
        // Registrar el receiver dinámicamente
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(wifiReceiver, filter)
        receiverRegistrado = true

        onDispose {
            // Desregistrar cuando se sale de la pantalla
            try {
                context.unregisterReceiver(wifiReceiver)
                receiverRegistrado = false
            } catch (e: Exception) {
                // Ya estaba desregistrado
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Prueba Broadcast Receiver",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (receiverRegistrado)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (receiverRegistrado)
                        "✅ Receiver ACTIVO"
                    else
                        "❌ Receiver INACTIVO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Instrucciones:",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "1. Mantén esta pantalla abierta\n" +
                            "2. Activa/desactiva el WiFi desde configuración\n" +
                            "3. Deberías ver un Toast emergente\n" +
                            "4. Revisa el Logcat para ver los logs",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ℹ️ Este receiver detecta cambios en la conectividad WiFi " +
                    "y muestra notificaciones cuando se conecta/desconecta.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}