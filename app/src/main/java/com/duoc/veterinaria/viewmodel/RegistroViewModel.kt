package com.duoc.veterinaria.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.model.*
import com.duoc.veterinaria.data.repository.AtencionRepository
import com.duoc.veterinaria.data.repository.AtencionRepositoryImpl
import com.duoc.veterinaria.data.repository.ClienteRepository
import com.duoc.veterinaria.data.repository.ClienteRepositoryImpl
import com.duoc.veterinaria.data.repository.RepositoryProvider
import com.duoc.veterinaria.data.service.VeterinariaService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel para la pantalla de registro de atenciones.
 *
 * Principio SRP: Solo maneja la lógica de registro de atenciones.
 * Principio DIP: Depende de interfaces (repositorios) no de implementaciones.
 */
class RegistroViewModel(
    private val atencionRepository: AtencionRepository = RepositoryProvider.atencionRepository,
    private val clienteRepository: ClienteRepository = RepositoryProvider.clienteRepository,
    private val service: VeterinariaService = VeterinariaService()
) : ViewModel() {

    // Estados de la UI
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registroCompletado = MutableLiveData<Boolean>()
    val registroCompletado: LiveData<Boolean> = _registroCompletado

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Datos temporales del formulario
    var clienteActual: Cliente? = null
    var mascotaActual: Mascota? = null
    var consultaActual: Consulta? = null

    /**
     * Obtiene los tipos de atención disponibles.
     * Principio ISP: Solo expone lo necesario para la UI.
     */
    fun obtenerTiposAtencion(): List<Pair<String, Double>> {
        return service.obtenerTiposAtencion()
    }

    fun obtenerMedicamentosGenerales(): List<Medicamento> {
        return service.obtenerMedicamentosGenerales()
    }

    fun obtenerVacunas(): List<Medicamento> {
        return service.obtenerVacunas()
    }

    fun calcularPrecioFinal(medicamento: Medicamento): Double {
        return service.obtenerPrecioFinalMedicamento(medicamento)
    }

    fun obtenerDetalleDescuento(medicamento: Medicamento): String {
        return service.obtenerDetalleDescuento(medicamento)
    }

    /**
     * Valida y guarda el cliente.
     * Principio SRP: Separación de validación y guardado.
     */
    fun validarYGuardarCliente(cliente: Cliente): Boolean {
        return if (clienteRepository.validarCliente(cliente)) {
            clienteRepository.guardarCliente(cliente)
                .onSuccess { clienteActual = it }
                .isSuccess
        } else {
            _errorMessage.value = "Datos del cliente inválidos"
            false
        }
    }

    /**
     * Registra una atención completa.
     * Principio KISS: Lógica directa sin complejidad innecesaria.
     */
    fun registrarAtencion(
        dueno: Cliente,
        mascota: Mascota,
        consulta: Consulta,
        medicamento: Medicamento,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val fechaHoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                val registro = RegistroAtencion(
                    dueno = dueno,
                    mascota = mascota,
                    consulta = consulta,
                    medicamento = medicamento,
                    veterinario = service.obtenerNombreVeterinario(),
                    fecha = fechaHoy,
                    precioFinalMedicamento = calcularPrecioFinal(medicamento),
                    detalleDescuento = obtenerDetalleDescuento(medicamento)
                )

                atencionRepository.guardarAtencion(registro)
                    .onSuccess {
                        _registroCompletado.value = true
                        onComplete()
                    }
                    .onFailure { error ->
                        _errorMessage.value = "Error al guardar: ${error.message}"
                    }

            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Registra atención sin medicamento (para consultas generales).
     */
    fun registrarAtencionSinMedicamento(
        dueno: Cliente,
        mascota: Mascota,
        consulta: Consulta,
        onComplete: () -> Unit
    ) {
        val medicamentoVacio = Medicamento("N/A", 0.0, 0)
        registrarAtencion(dueno, mascota, consulta, medicamentoVacio, onComplete)
    }

    /**
     * Limpia el estado de error.
     */
    fun limpiarError() {
        _errorMessage.value = null
    }
}