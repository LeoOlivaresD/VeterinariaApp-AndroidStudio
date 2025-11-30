package com.duoc.veterinaria.data.model

data class Consulta(
    val id: Int,
    val descripcion: String,
    val costoConsulta: Double,
    val estado: String,
    val medicamentos: List<Medicamento> = emptyList()
)

