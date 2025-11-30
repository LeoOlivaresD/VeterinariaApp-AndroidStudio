package com.duoc.veterinaria.service

import com.duoc.veterinaria.model.Medicamento
import com.duoc.veterinaria.utils.Validaciones
import java.util.Calendar

class VeterinariaService {

    // Proveedor de Tipos de Atención y sus costos base
    fun obtenerTiposAtencion(): List<Pair<String, Double>> {
        return listOf(
            "Consulta general" to 15000.0,
            "Urgencia" to 20000.0,
            "Vacunación" to 10000.0,
            "Control" to 12000.0
        )
    }

    // Lista específica de Vacunas (para tipo "Vacunación")
    fun obtenerVacunas(): List<Medicamento> {
        return listOf(
            Medicamento("Vacuna Antirabica", 8000.0, 30),
            Medicamento("Antiparasitaria", 15000.0, 50),
            Medicamento("Triple Felina", 9000.0, 20)
        )
    }

    // Lista específica de Medicamentos (para tipo "Urgencia")
    fun obtenerMedicamentosGenerales(): List<Medicamento> {
        return listOf(
            Medicamento("Mulcatel", 1000.0, 10),
            Medicamento("Meloxicam", 12000.0, 13),
            Medicamento("Amoxicilina-clavulánico", 3000.0, 22)
        )
    }

    // Lógica de precio con descuento (Promo 10-20 del mes O anotación @Promocionable)
    fun obtenerPrecioFinalMedicamento(med: Medicamento): Double {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Verifica si estamos en días de promo (10 al 20)
        val esPeriodoPromo = Validaciones.estaEnPeriodoPromocional(diaHoy)

        // Si es periodo promo, o si el medicamento tiene la anotación (la lógica interna de la clase lo revisa)
        // En tu lógica original, si está en periodo promo se aplica el descuento.
        // Además, calcularPrecioConDescuento() ya verifica la anotación.

        return if (esPeriodoPromo) {
            // Forzamos el cálculo con descuento si es el periodo correcto
            med.precio * 0.8 // 20% descuento manual o usar la lógica de la clase si prefieres
        } else {
            // Si no es fecha promo, confiamos en la anotación del modelo
            med.calcularPrecioConDescuento()
        }
    }
}