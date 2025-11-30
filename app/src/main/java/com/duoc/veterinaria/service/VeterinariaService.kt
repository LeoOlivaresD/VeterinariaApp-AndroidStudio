package com.duoc.veterinaria.service

import com.duoc.veterinaria.annotations.Promocionable
import com.duoc.veterinaria.model.Medicamento
import com.duoc.veterinaria.utils.Validaciones
import java.util.Calendar

class VeterinariaService {

    fun obtenerTiposAtencion(): List<Pair<String, Double>> {
        return listOf(
            "Consulta general" to 15000.0,
            "Urgencia" to 20000.0,
            "Vacunación" to 10000.0,
            "Control" to 12000.0
        )
    }

    fun obtenerVacunas(): List<Medicamento> {
        return listOf(
            Medicamento("Vacuna Antirabica", 8000.0, 30),
            Medicamento("Antiparasitaria", 15000.0, 50),
            Medicamento("Triple Felina", 9000.0, 20)
        )
    }

    fun obtenerMedicamentosGenerales(): List<Medicamento> {
        return listOf(
            Medicamento("Mulcatel", 1000.0, 10),
            Medicamento("Meloxicam", 12000.0, 13),
            Medicamento("Amoxicilina-clavulánico", 3000.0, 22)
        )
    }

    // 1. Nuevo: Obtener nombre del veterinario de turno
    fun obtenerNombreVeterinario(): String {
        return "Dra. López" // Dato simulado
    }

    fun obtenerPrecioFinalMedicamento(med: Medicamento): Double {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val esPeriodoPromo = Validaciones.estaEnPeriodoPromocional(diaHoy)

        return if (esPeriodoPromo) {
            med.precio * 0.8
        } else {
            med.calcularPrecioConDescuento()
        }
    }

    // 2. Generar texto explicativo del descuento
    fun obtenerDetalleDescuento(med: Medicamento): String {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Prioridad 1: Fecha Promocional
        if (Validaciones.estaEnPeriodoPromocional(diaHoy)) {
            return "20% dcto. (Semana Promocional)"
        }

        // Anotación del producto
        // Usamos reflection para ver si tiene la anotación @Promocionable
        val anotacion = med::class.annotations.find { it is Promocionable } as? Promocionable
        if (anotacion != null) {
            return "${(anotacion.descuento * 100).toInt()}% dcto. (${anotacion.descripcion})"
        }

        return "" // Sin descuento
    }
}