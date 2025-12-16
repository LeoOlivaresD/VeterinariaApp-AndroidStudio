package com.duoc.veterinaria.data.repository

import com.duoc.veterinaria.data.model.Cliente

/**
 * Repositorio para gestionar operaciones relacionadas con Cliente.
 * Actúa como intermediario entre el ViewModel y la fuente de datos.
 *
 * Principio SRP (Single Responsibility):
 * Solo se encarga de operaciones CRUD de Cliente.
 */
interface ClienteRepository {
    fun validarCliente(cliente: Cliente): Boolean
    fun guardarCliente(cliente: Cliente): Result<Cliente>
}

class ClienteRepositoryImpl : ClienteRepository {

    // Simulación de almacenamiento local
    private val clientesRegistrados = mutableListOf<Cliente>()

    override fun validarCliente(cliente: Cliente): Boolean {
        return cliente.nombre.isNotBlank() &&
                cliente.email.isNotBlank() &&
                cliente.telefono.isNotBlank()
    }

    override fun guardarCliente(cliente: Cliente): Result<Cliente> {
        return try {
            if (!validarCliente(cliente)) {
                Result.failure(IllegalArgumentException("Datos de cliente inválidos"))
            } else {
                clientesRegistrados.add(cliente)
                Result.success(cliente)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}