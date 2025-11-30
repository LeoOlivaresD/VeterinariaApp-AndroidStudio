package com.duoc.veterinaria.data.model

data class Agenda(
    val veterinario: Veterinario,
    val citas: MutableList<Consulta>
)
