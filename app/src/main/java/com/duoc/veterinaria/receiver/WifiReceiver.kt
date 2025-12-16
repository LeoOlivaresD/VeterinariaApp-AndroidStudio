package com.duoc.veterinaria.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast

class WifiReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "WifiReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "🔔 onReceive llamado - Action: ${intent?.action}")

        if (context == null) {
            Log.e(TAG, "❌ Context es null")
            return
        }

        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isConnected = isWifiConnected(context)

            Log.d(TAG, "📶 Estado WiFi: ${if (isConnected) "CONECTADO" else "DESCONECTADO"}")

            if (isConnected) {
                val mensaje = "📶 WiFi conectado - Recordatorio: Revisa las consultas pendientes"
                Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                Log.d(TAG, "✅ Toast mostrado: $mensaje")
            } else {
                val mensaje = "📴 WiFi desconectado"
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                Log.d(TAG, "✅ Toast mostrado: $mensaje")
            }
        } else {
            Log.w(TAG, "⚠️ Action no reconocida: ${intent?.action}")
        }
    }

    private fun isWifiConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            if (network == null) {
                Log.d(TAG, "No hay red activa")
                return false
            }
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            if (capabilities == null) {
                Log.d(TAG, "No hay capabilities")
                return false
            }
            val hasWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            Log.d(TAG, "Tiene WiFi: $hasWifi")
            hasWifi
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            val isWifi = networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
            Log.d(TAG, "Es WiFi (legacy): $isWifi")
            isWifi
        }
    }
}