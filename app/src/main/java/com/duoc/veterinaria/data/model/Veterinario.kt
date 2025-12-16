package com.duoc.veterinaria.data.model

data class Veterinario(
    val nombre: String,
    val especialidad: String,
    val disponibilidad: List<String>
)
