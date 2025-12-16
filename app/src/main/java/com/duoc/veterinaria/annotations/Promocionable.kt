package com.duoc.veterinaria.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable(
    val descuento: Double,
    val descripcion: String = "Producto en promoción"
)