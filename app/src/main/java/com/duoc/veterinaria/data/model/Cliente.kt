package com.duoc.veterinaria.data.model

data class Cliente(
    val nombre: String,
    val email: String,
    val telefono: String
) {

    // Sobrescritura de equals y hashCode para comparación personalizada
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false
        return nombre == other.nombre && email == other.email
    }

    override fun hashCode(): Int {
        return nombre.hashCode() * 31 + email.hashCode()
    }
}