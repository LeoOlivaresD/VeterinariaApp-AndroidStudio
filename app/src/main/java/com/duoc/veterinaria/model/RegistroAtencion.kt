package com.duoc.veterinaria.model

// Esta es la clase que define qué datos guardamos de cada atención
data class RegistroAtencion(
    val dueno: Cliente,
    val mascota: Mascota,
    val consulta: Consulta,
    val medicamento: Medicamento
)