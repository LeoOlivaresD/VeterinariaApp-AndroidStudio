package com.duoc.veterinaria

import org.junit.Test
import org.junit.Assert.*

class ClienteValidacionTest {

    @Test
    fun `validar email - formato correcto retorna true`() {
        val email = "juan@correo.com"
        val resultado = esEmailValido(email)
        assertTrue("Email válido debe retornar true", resultado)
    }

    @Test
    fun `validar email - sin arroba retorna false`() {
        val email = "juancorreo.com"
        val resultado = esEmailValido(email)
        assertFalse("Email sin @ debe retornar false", resultado)
    }

    @Test
    fun `validar email - vacio retorna false`() {
        val email = ""
        val resultado = esEmailValido(email)
        assertFalse("Email vacío debe retornar false", resultado)
    }

    @Test
    fun `validar telefono - 9 digitos retorna true`() {
        val telefono = "912345678"
        val resultado = esTelefonoValido(telefono)
        assertTrue("Teléfono con 9 dígitos debe retornar true", resultado)
    }

    @Test
    fun `validar telefono - menos de 9 digitos retorna false`() {
        val telefono = "12345"
        val resultado = esTelefonoValido(telefono)
        assertFalse("Teléfono con menos de 9 dígitos debe retornar false", resultado)
    }

    @Test
    fun `validar telefono - con letras retorna false`() {
        val telefono = "abc123456"
        val resultado = esTelefonoValido(telefono)
        assertFalse("Teléfono con letras debe retornar false", resultado)
    }

    @Test
    fun `validar nombre - no vacio retorna true`() {
        val nombre = "Juan Pérez"
        val resultado = esNombreValido(nombre)
        assertTrue("Nombre no vacío debe retornar true", resultado)
    }

    @Test
    fun `validar nombre - vacio retorna false`() {
        val nombre = ""
        val resultado = esNombreValido(nombre)
        assertFalse("Nombre vacío debe retornar false", resultado)
    }

    @Test
    fun `validar nombre - solo espacios retorna false`() {
        val nombre = "   "
        val resultado = esNombreValido(nombre)
        assertFalse("Nombre solo con espacios debe retornar false", resultado)
    }

    // ========================================
    // FUNCIONES DE VALIDACIÓN
    // ========================================

    private fun esEmailValido(email: String): Boolean {
        if (email.isBlank()) return false
        return email.contains("@") && email.contains(".")
    }

    private fun esTelefonoValido(telefono: String): Boolean {
        if (telefono.length < 9) return false
        return telefono.all { it.isDigit() }
    }

    private fun esNombreValido(nombre: String): Boolean {
        return nombre.isNotBlank()
    }
}