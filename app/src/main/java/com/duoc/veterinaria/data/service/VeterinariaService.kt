package com.duoc.veterinaria.data.service

import com.duoc.veterinaria.annotations.Promocionable
import com.duoc.veterinaria.data.model.Medicamento
import com.duoc.veterinaria.utils.Validaciones
import java.util.Calendar

/**
 * Servicio para lógica de negocio de la veterinaria.
 *
 * Principio SRP: Solo contiene lógica de negocio pura (cálculos, reglas).
 * Principio OCP: Podemos extender sin modificar código existente.
 * Principio ISP: Interfaces segregadas para cada responsabilidad.
 */
interface IConsultaService {
    fun obtenerTiposAtencion(): List<Pair<String, Double>>
}

interface IMedicamentoService {
    fun obtenerVacunas(): List<Medicamento>
    fun obtenerMedicamentosGenerales(): List<Medicamento>
    fun calcularPrecioConDescuento(medicamento: Medicamento): Double
    fun obtenerDetalleDescuento(medicamento: Medicamento): String
}

interface IVeterinarioService {
    fun obtenerNombreVeterinario(): String
}

/**
 * Implementación del servicio veterinario.
 * Principio KISS: Métodos simples y directos.
 */
class VeterinariaService : IConsultaService, IMedicamentoService, IVeterinarioService {

    // --- Implementación IConsultaService ---

    override fun obtenerTiposAtencion(): List<Pair<String, Double>> {
        return listOf(
            "Consulta general" to 15000.0,
            "Urgencia" to 20000.0,
            "Vacunación" to 10000.0,
            "Control" to 12000.0
        )
    }

    // --- Implementación IMedicamentoService ---

    override fun obtenerVacunas(): List<Medicamento> {
        return listOf(
            Medicamento("Vacuna Antirabica", 8000.0, 30),
            Medicamento("Antiparasitaria", 15000.0, 50),
            Medicamento("Triple Felina", 9000.0, 20)
        )
    }

    override fun obtenerMedicamentosGenerales(): List<Medicamento> {
        return listOf(
            Medicamento("Mulcatel", 1000.0, 10),
            Medicamento("Meloxicam", 12000.0, 13),
            Medicamento("Amoxicilina-clavulánico", 3000.0, 22)
        )
    }

    /**
     * Calcula el precio final con descuentos aplicados.
     * Principio KISS: Lógica de descuento simple y clara.
     */
    override fun calcularPrecioConDescuento(medicamento: Medicamento): Double {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val esPeriodoPromo = Validaciones.estaEnPeriodoPromocional(diaHoy)

        return if (esPeriodoPromo) {
            // Descuento por periodo promocional (20%)
            medicamento.precio * 0.8
        } else {
            // Descuento por anotación @Promocionable
            medicamento.calcularPrecioConDescuento()
        }
    }

    /**
     * Genera texto explicativo del descuento aplicado.
     */
    override fun obtenerDetalleDescuento(medicamento: Medicamento): String {
        val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // Prioridad 1: Fecha Promocional
        if (Validaciones.estaEnPeriodoPromocional(diaHoy)) {
            return "20% dcto. (Semana Promocional)"
        }

        // Prioridad 2: Anotación @Promocionable
        val anotacion = medicamento::class.annotations
            .find { it is Promocionable } as? Promocionable

        if (anotacion != null) {
            val porcentaje = (anotacion.descuento * 100).toInt()
            return "$porcentaje% dcto. (${anotacion.descripcion})"
        }

        return "" // Sin descuento
    }

    // --- Implementación IVeterinarioService ---

    override fun obtenerNombreVeterinario(): String {
        // En una app real, esto vendría de un repositorio o API
        return "Dra. López"
    }

    // --- Método de conveniencia (mantiene compatibilidad) ---

    /**
     * Método público que combina cálculo y detalle.
     * Principio DRY: Evita duplicar lógica.
     */
    fun obtenerPrecioFinalMedicamento(medicamento: Medicamento): Double {
        return calcularPrecioConDescuento(medicamento)
    }
}