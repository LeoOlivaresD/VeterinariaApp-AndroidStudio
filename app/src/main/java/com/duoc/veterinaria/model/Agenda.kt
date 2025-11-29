package com.duoc.veterinaria.model

data class Agenda(
    val veterinario: Veterinario,
    val citas: MutableList<Consulta>
)
