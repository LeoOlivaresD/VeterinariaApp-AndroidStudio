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

    // Proveedor de Medicamentos disponibles
    fun obtenerMedicamentosDisponibles(): List<Medicamento> {
        return listOf(
            Medicamento("Vacuna Rabia", 8000.0, 30),
            Medicamento("Antiparasitario", 15000.0, 50),
            Medicamento("Antiinflamatorio", 9000.0, 20)
        )
    }

    // Lógica para determinar el precio final (migrada de tu lógica original)
    fun obtenerPrecioFinalMedicamento(med: Medicamento): Double {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Verificamos si aplica descuento por fecha O por anotación (según tu lógica original)
        val esPeriodoPromo = Validaciones.estaEnPeriodoPromocional(diaHoy)

        // En tu modelo, calcularPrecioConDescuento() ya verifica la anotación internamente.
        // Aquí podrías agregar lógica extra si el periodo promocional afectara a items sin anotación.
        // Por ahora, devolvemos el precio calculado por el modelo.
        return med.calcularPrecioConDescuento()
    }
}