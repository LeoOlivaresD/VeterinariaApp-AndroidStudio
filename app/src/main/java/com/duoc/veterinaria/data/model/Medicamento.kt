package com.duoc.veterinaria.data.model

import com.duoc.veterinaria.annotations.Promocionable

@Promocionable(descuento = 0.20, descripcion = "Medicamento con 20% de descuento")
data class Medicamento(
    val nombre: String,
    val precio: Double,
    val stock: Int
) {
    operator fun plus(cantidad: Int): Medicamento {
        return this.copy(stock = this.stock + cantidad)
    }

    fun calcularPrecioConDescuento(): Double {
        val anotacion = this::class.annotations.find { it is Promocionable } as? Promocionable
        return if (anotacion != null) {
            precio * (1 - anotacion.descuento)
        } else {
            precio
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Medicamento && nombre == other.nombre && precio == other.precio
    }

    override fun hashCode(): Int {
        return nombre.hashCode() * 31 + precio.hashCode()
    }
}
