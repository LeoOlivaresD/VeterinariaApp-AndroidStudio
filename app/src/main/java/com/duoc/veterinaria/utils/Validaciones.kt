package com.duoc.veterinaria.utils

object Validaciones {

    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun validarEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    fun formatearTelefono(telefono: String): String {
        val digitos = telefono.replace(Regex("[^0-9]"), "")
        return when {
            digitos.length == 9 && digitos.startsWith("9") ->
                "+56 ${digitos[0]} ${digitos.substring(1, 5)} ${digitos.substring(5)}"
            digitos.length == 11 && digitos.startsWith("56") ->
                "+${digitos.substring(0, 2)} ${digitos[2]} ${digitos.substring(3, 7)} ${digitos.substring(7)}"
            else -> telefono
        }
    }

    fun validarRangoFecha(fecha: String, rangoInicio: Int, rangoFin: Int): Boolean {
        val partes = fecha.split("-")
        if (partes.size != 2) return false
        val dia = partes[0].toIntOrNull() ?: return false
        return dia in rangoInicio..rangoFin
    }

    fun validarCantidad(cantidad: Int): Boolean {
        return cantidad in 1..100
    }

    fun estaEnPeriodoPromocional(dia: Int, inicio: Int = 10, fin: Int = 20): Boolean {
        return dia in inicio..fin
    }
}