package com.duoc.veterinaria.data.repository

import com.duoc.veterinaria.data.model.RegistroAtencion
import kotlinx.coroutines.delay

/**
 * Repositorio para gestionar el registro de atenciones veterinarias.
 *
 * Principio OCP (Open/Closed):
 * Podemos extender funcionalidad sin modificar código existente.
 *
 * Principio DIP (Dependency Inversion):
 * Depende de abstracción (interfaz) no de implementación concreta.
 */
interface AtencionRepository {
    suspend fun guardarAtencion(registro: RegistroAtencion): Result<RegistroAtencion>
    suspend fun obtenerTodasLasAtenciones(): Result<List<RegistroAtencion>>
    suspend fun obtenerAtencionPorId(id: Int): Result<RegistroAtencion?>
}

class AtencionRepositoryImpl : AtencionRepository {

    // Simulación de base de datos local
    private val atenciones = mutableListOf<RegistroAtencion>()

    override suspend fun guardarAtencion(registro: RegistroAtencion): Result<RegistroAtencion> {
        return try {
            // Simular operación de red/BD con delay
            delay(500)
            atenciones.add(registro)
            Result.success(registro)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenerTodasLasAtenciones(): Result<List<RegistroAtencion>> {
        return try {
            delay(300)
            Result.success(atenciones.toList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obtenerAtencionPorId(id: Int): Result<RegistroAtencion?> {
        return try {
            delay(200)
            val atencion = atenciones.getOrNull(id)
            Result.success(atencion)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}