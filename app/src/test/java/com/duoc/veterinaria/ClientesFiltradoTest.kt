package com.duoc.veterinaria

import com.duoc.veterinaria.data.local.entity.ClienteEntity
import org.junit.Test
import org.junit.Assert.*

class ClientesFiltradoTest {

    @Test
    fun `filtrar clientes - query vacio retorna todos`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321"),
            ClienteEntity(3, "Carlos Ruiz", "carlos@correo.com", "956781234")
        )

        val resultado = filtrarClientes(clientes, "")

        assertEquals("Query vacío debe retornar todos", 3, resultado.size)
    }

    @Test
    fun `filtrar clientes - buscar por nombre`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321"),
            ClienteEntity(3, "Carlos Ruiz", "carlos@correo.com", "956781234")
        )

        val resultado = filtrarClientes(clientes, "juan")

        assertEquals("Debe encontrar 1 cliente", 1, resultado.size)
        assertEquals("Debe ser Juan Pérez", "Juan Pérez", resultado[0].nombre)
    }

    @Test
    fun `filtrar clientes - buscar por email`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321"),
            ClienteEntity(3, "Carlos Ruiz", "carlos@correo.com", "956781234")
        )

        val resultado = filtrarClientes(clientes, "maria@")

        assertEquals("Debe encontrar 1 cliente", 1, resultado.size)
        assertEquals("Debe ser María López", "María López", resultado[0].nombre)
    }

    @Test
    fun `filtrar clientes - buscar por telefono`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321"),
            ClienteEntity(3, "Carlos Ruiz", "carlos@correo.com", "956781234")
        )

        val resultado = filtrarClientes(clientes, "9876")

        assertEquals("Debe encontrar 1 cliente", 1, resultado.size)
        assertEquals("Debe ser María López", "María López", resultado[0].nombre)
    }

    @Test
    fun `filtrar clientes - query sin resultados retorna vacio`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321")
        )

        val resultado = filtrarClientes(clientes, "xyz123")

        assertEquals("Query sin matches debe retornar vacío", 0, resultado.size)
    }

    @Test
    fun `filtrar clientes - case insensitive`() {
        val clientes = listOf(
            ClienteEntity(1, "Juan Pérez", "juan@correo.com", "912345678"),
            ClienteEntity(2, "María López", "maria@correo.com", "987654321")
        )

        val resultado = filtrarClientes(clientes, "JUAN")

        assertEquals("Búsqueda debe ser case-insensitive", 1, resultado.size)
        assertEquals("Debe encontrar a Juan", "Juan Pérez", resultado[0].nombre)
    }

    // ========================================
    // FUNCIÓN DE FILTRADO
    // ========================================

    private fun filtrarClientes(
        clientes: List<ClienteEntity>,
        query: String
    ): List<ClienteEntity> {
        if (query.isBlank()) return clientes

        val queryLower = query.lowercase()

        return clientes.filter { cliente ->
            cliente.nombre.lowercase().contains(queryLower) ||
                    cliente.email.lowercase().contains(queryLower) ||
                    cliente.telefono.contains(queryLower)
        }
    }
}