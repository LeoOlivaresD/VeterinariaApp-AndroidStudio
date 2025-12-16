package com.duoc.veterinaria.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.duoc.veterinaria.MainActivity
import com.duoc.veterinaria.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Servicio en segundo plano para enviar recordatorios de consultas.
 *
 * Principio SRP: Solo se encarga de gestionar notificaciones de recordatorios.
 * Cumple con el requisito: "Implementa un servicio en segundo plano para enviar
 * recordatorios de consultas o notificaciones de seguimiento para las mascotas."
 */
class RecordatorioService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Default + serviceJob)

    companion object {
        private const val CHANNEL_ID = "veterinaria_recordatorios"
        private const val NOTIFICATION_ID = 1
        const val ACTION_START = "com.duoc.veterinaria.action.START_SERVICE"
        const val ACTION_STOP = "com.duoc.veterinaria.action.STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Iniciar como servicio en foreground
                startForeground(NOTIFICATION_ID, createForegroundNotification())
                iniciarRecordatorios()
            }
            ACTION_STOP -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        serviceJob.cancel()
        super.onDestroy()
    }

    /**
     * Crea el canal de notificaciones (requerido para Android 8+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios Veterinaria",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de recordatorios de consultas"
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Crea la notificación permanente del servicio foreground
     */
    private fun createForegroundNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Servicio de Recordatorios")
            .setContentText("Monitoreando consultas veterinarias")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    /**
     * Simula el envío periódico de recordatorios
     */
    private fun iniciarRecordatorios() {
        serviceScope.launch {
            var contador = 0
            while (true) {
                delay(3000) // Cada 3 segundos (para demostración)
                contador++
                enviarNotificacionRecordatorio(contador)
            }
        }
    }

    /**
     * Envía una notificación de recordatorio
     */
    private fun enviarNotificacionRecordatorio(numero: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Recordatorio de Consulta #$numero")
            .setContentText("No olvides las consultas pendientes de tus mascotas")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(numero + 100, notification)
    }
}