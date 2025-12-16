package com.duoc.veterinaria.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.model.Medicamento
import com.duoc.veterinaria.data.model.RegistroAtencion
import com.duoc.veterinaria.data.service.VeterinariaService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VeterinariaViewModel : ViewModel() {

    // 1. Instancia del servicio (Lógica de negocio pura)
    private val service = VeterinariaService()

    // 2. Estado de la lista de registros (Observable por la UI)
    private val _registros = MutableLiveData<List<RegistroAtencion>>(emptyList())
    val registros: LiveData<List<RegistroAtencion>> = _registros

    // 3. Estado de carga (para mostrar el círculo de progreso)
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // --- Funciones Puente (Exponen datos del servicio a la UI) ---
    fun obtenerTiposAtencion() = service.obtenerTiposAtencion()
    fun obtenerMedicamentosGenerales() = service.obtenerMedicamentosGenerales()
    fun obtenerVacunas() = service.obtenerVacunas()
    fun obtenerNombreVeterinario() = service.obtenerNombreVeterinario()

    fun calcularPrecioFinal(med: Medicamento) = service.obtenerPrecioFinalMedicamento(med)
    fun obtenerDetalleDescuento(med: Medicamento) = service.obtenerDetalleDescuento(med)

    // --- Lógica de Acciones (Coroutines) ---

    // Función para agregar registro simulando una operación asíncrona (como guardar en BD)
    fun agregarRegistro(registro: RegistroAtencion, onResult: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true // Activar carga

            delay(1500) // Simular espera de red/bd (1.5 seg)

            // Actualizar la lista usando LiveData
            val listaActual = _registros.value.orEmpty().toMutableList()
            listaActual.add(registro)
            _registros.value = listaActual

            _isLoading.value = false // Desactivar carga
            onResult() // Avisar a la UI que terminó
        }
    }
}