package com.duoc.veterinaria.data.model

data class Pedido(
    val idPedido: Int,
    val cliente: Cliente,
    val medicamentos: List<Medicamento>,
    val total: Double
) {

    // Sobrecarga del operador + para combinar pedidos
    operator fun plus(otroPedido: Pedido): Pedido {
        val nuevosMedicamentos = this.medicamentos + otroPedido.medicamentos
        val nuevoTotal = this.total + otroPedido.total
        return Pedido(
            idPedido = this.idPedido,
            cliente = this.cliente,
            medicamentos = nuevosMedicamentos,
            total = nuevoTotal
        )
    }
}