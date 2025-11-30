package com.duoc.veterinaria.model

data class RegistroAtencion(
    val dueno: Cliente,
    val mascota: Mascota,
    val consulta: Consulta,
    val medicamento: Medicamento,
    val veterinario: String,
    val fecha: String,
    val precioFinalMedicamento: Double, // Guardamos el precio exacto que se cobró
    val detalleDescuento: String // Texto explicativo (ej: "20% por Promo")
)