package com.duoc.veterinaria.model

data class Veterinario(
    val nombre: String,
    val especialidad: String,
    val disponibilidad: List<String>
)
