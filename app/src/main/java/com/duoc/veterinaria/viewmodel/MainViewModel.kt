package com.duoc.veterinaria.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.repository.AtencionRepository
import com.duoc.veterinaria.data.repository.AtencionRepositoryImpl
import com.duoc.veterinaria.data.repository.RepositoryProvider
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla principal (WelcomeScreen).
 *
 * Principio SRP: Solo maneja lógica de presentación de la pantalla Welcome.
 * Principio KISS: Mantiene lógica simple y directa.
 */
class MainViewModel(
    private val atencionRepository: AtencionRepository = RepositoryProvider.atencionRepository  // ← USAR SINGLETON
) : ViewModel() {

    // Estado de la UI
    private val _totalMascotas = MutableLiveData(0)
    val totalMascotas: LiveData<Int> = _totalMascotas

    private val _totalConsultas = MutableLiveData(0)
    val totalConsultas: LiveData<Int> = _totalConsultas

    private val _ultimoDueno = MutableLiveData("N/A")
    val ultimoDueno: LiveData<String> = _ultimoDueno

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        cargarEstadisticas()
    }

    /**
     * Carga las estadísticas desde el repositorio.
     * Principio KISS: Método simple y directo.
     */
    fun cargarEstadisticas() {
        viewModelScope.launch {
            _isLoading.value = true

            atencionRepository.obtenerTodasLasAtenciones()
                .onSuccess { registros ->
                    _totalMascotas.value = registros.map { it.mascota.nombre }.distinct().size
                    _totalConsultas.value = registros.size
                    _ultimoDueno.value = registros.lastOrNull()?.dueno?.nombre ?: "N/A"
                }
                .onFailure {
                    // Manejar error (podríamos agregar un LiveData para errores)
                    _totalMascotas.value = 0
                    _totalConsultas.value = 0
                    _ultimoDueno.value = "Error al cargar"
                }

            _isLoading.value = false
        }
    }
}