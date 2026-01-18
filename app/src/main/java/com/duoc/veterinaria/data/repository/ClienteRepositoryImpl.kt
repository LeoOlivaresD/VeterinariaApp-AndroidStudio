package com.duoc.veterinaria.data.repository

import com.duoc.veterinaria.data.model.Cliente

interface ClienteRepository {
    fun validarCliente(cliente: Cliente): Boolean
    fun guardarCliente(cliente: Cliente): Result<Cliente>
    fun obtenerClientes(): List<Cliente>
}

class ClienteRepositoryImpl : ClienteRepository {

    private val clientes = mutableListOf<Cliente>()

    override fun validarCliente(cliente: Cliente): Boolean {
        return cliente.nombre.isNotBlank() &&
                cliente.email.isNotBlank() &&
                cliente.telefono.isNotBlank()
    }

    override fun guardarCliente(cliente: Cliente): Result<Cliente> {
        return try {
            clientes.add(cliente)
            Result.success(cliente)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun obtenerClientes(): List<Cliente> {
        return clientes.toList()
    }
}