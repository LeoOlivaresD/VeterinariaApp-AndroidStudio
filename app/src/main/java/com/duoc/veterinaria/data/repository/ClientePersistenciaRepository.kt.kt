package com.duoc.veterinaria.data.repository

import com.duoc.veterinaria.data.local.dao.ClienteDao
import com.duoc.veterinaria.data.local.entity.ClienteEntity
import com.duoc.veterinaria.data.local.prefs.ClientesPrefs
import com.duoc.veterinaria.data.local.sqlite.ClientesLogDbHelper
import com.duoc.veterinaria.utils.AppLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class ClientePersistenciaRepository(
    private val dao: ClienteDao,
    private val prefs: ClientesPrefs,
    private val logs: ClientesLogDbHelper
) {
    private val TAG = AppLogger.Tags.REPOSITORY

    init {
        AppLogger.i(TAG, "ClientePersistenciaRepository inicializado")
    }

    fun clientesFlow(): Flow<List<ClienteEntity>> {
        AppLogger.d(TAG, "Creando Flow de clientes desde Room")

        return dao.getAll()
            .onStart {
                AppLogger.d(TAG, "Flow iniciado - comenzando observación de clientes")
            }
            .catch { exception ->
                AppLogger.e(TAG, "Error en Flow de clientes", exception)
                emit(emptyList())
            }
    }

    suspend fun registrarCliente(nombre: String, email: String, telefono: String): Boolean {
        AppLogger.methodEntry(TAG, "registrarCliente", "email=$email")

        val startTime = System.currentTimeMillis()

        return try {
            // Paso 1: Validar datos
            AppLogger.d(TAG, "Validando datos del cliente")

            if (nombre.isBlank() || email.isBlank() || telefono.isBlank()) {
                throw IllegalArgumentException("Datos incompletos: nombre, email y teléfono son obligatorios")
            }

            // Paso 2: Insertar en Room Database
            AppLogger.d(TAG, "Insertando cliente en Room Database")
            val clienteEntity = ClienteEntity(
                nombre = nombre.trim(),
                email = email.trim(),
                telefono = telefono.trim()
            )

            val id = try {
                dao.insert(clienteEntity)
            } catch (e: Exception) {
                AppLogger.e(TAG, "Error al insertar en Room Database", e)
                throw Exception("Error de base de datos: ${e.message}", e)
            }

            if (id <= 0) {
                throw Exception("La base de datos retornó un ID inválido: $id")
            }

            AppLogger.i(TAG, "Cliente insertado en Room con ID: $id")

            // Paso 3: Registrar en logs SQLite
            AppLogger.d(TAG, "Registrando evento en SQLite logs")
            try {
                logs.addLog("Insert cliente id=$id email=$email")
                AppLogger.d(TAG, "Log SQLite registrado exitosamente")
            } catch (e: Exception) {
                AppLogger.w(TAG, "Advertencia: No se pudo registrar en logs SQLite", e)
                // No lanzamos excepción porque el registro principal ya se hizo
            }

            // Paso 4: Guardar en SharedPreferences
            AppLogger.d(TAG, "Guardando último email en SharedPreferences")
            try {
                prefs.setUltimoEmail(email)
                prefs.setUltimoGuardadoMillis(System.currentTimeMillis())
                AppLogger.d(TAG, "SharedPreferences actualizado exitosamente")
            } catch (e: Exception) {
                AppLogger.w(TAG, "Advertencia: No se pudo actualizar SharedPreferences", e)
                // No lanzamos excepción porque el registro principal ya se hizo
            }

            val duration = System.currentTimeMillis() - startTime
            AppLogger.performance(TAG, "Registro completo de cliente", duration)
            AppLogger.methodExit(TAG, "registrarCliente", "success=true, id=$id")

            true

        } catch (e: IllegalArgumentException) {
            AppLogger.w(TAG, "Validación fallida en registro", e)
            AppLogger.methodExit(TAG, "registrarCliente", "success=false - validación")
            throw e
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error crítico al registrar cliente", e)
            AppLogger.methodExit(TAG, "registrarCliente", "success=false - error")
            throw e
        }
    }

    suspend fun obtenerTodosLosClientes(): Result<List<ClienteEntity>> {
        AppLogger.methodEntry(TAG, "obtenerTodosLosClientes")

        return try {
            val clientes = dao.getAll()
            AppLogger.i(TAG, "Clientes obtenidos exitosamente")
            AppLogger.methodExit(TAG, "obtenerTodosLosClientes", "count=${clientes}")
            Result.success(emptyList())
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error al obtener clientes", e)
            AppLogger.methodExit(TAG, "obtenerTodosLosClientes", "error")
            Result.failure(e)
        }
    }

    fun obtenerUltimoEmailGuardado(): String? {
        AppLogger.methodEntry(TAG, "obtenerUltimoEmailGuardado")

        return try {
            val email = prefs.getUltimoEmail()
            AppLogger.d(TAG, "Email recuperado: ${if (email.isNotEmpty()) email else "ninguno"}")
            AppLogger.methodExit(TAG, "obtenerUltimoEmailGuardado", "email=$email")
            email.ifEmpty { null }
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error al recuperar último email", e)
            AppLogger.methodExit(TAG, "obtenerUltimoEmailGuardado", "error")
            null
        }
    }

    fun obtenerTimestampUltimoGuardado(): Long {
        AppLogger.methodEntry(TAG, "obtenerTimestampUltimoGuardado")

        return try {
            val timestamp = prefs.getUltimoGuardadoMillis()
            AppLogger.d(TAG, "Timestamp recuperado: $timestamp")
            AppLogger.methodExit(TAG, "obtenerTimestampUltimoGuardado", "timestamp=$timestamp")
            timestamp
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error al recuperar timestamp", e)
            AppLogger.methodExit(TAG, "obtenerTimestampUltimoGuardado", "error")
            0L
        }
    }
}