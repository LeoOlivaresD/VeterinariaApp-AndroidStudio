package com.duoc.veterinaria.model

data class Consulta(
    val idConsulta: Int,
    var descripcion: String,
    var costoConsulta: Double,
    var estado: String
)

