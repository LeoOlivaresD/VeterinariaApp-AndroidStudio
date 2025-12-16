package com.duoc.veterinaria.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.model.RegistroAtencion
import com.duoc.veterinaria.data.repository.AtencionRepository
import com.duoc.veterinaria.data.repository.AtencionRepositoryImpl
import com.duoc.veterinaria.data.repository.RepositoryProvider
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de historial de consultas (ResumenScreen).
 *
 * Principio SRP: Solo maneja la visualización del historial.
 * Principio KISS: Operaciones simples de lectura.
 */
class ConsultaViewModel(
    private val atencionRepository: AtencionRepository = RepositoryProvider.atencionRepository
) : ViewModel() {

    // Estado de la lista de atenciones
    private val _registros = MutableLiveData<List<RegistroAtencion>>(emptyList())
    val registros: LiveData<List<RegistroAtencion>> = _registros

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        cargarRegistros()
    }

    /**
     * Carga todos los registros desde el repositorio.
     * Principio KISS: Implementación directa sin lógica compleja.
     */
    fun cargarRegistros() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            atencionRepository.obtenerTodasLasAtenciones()
                .onSuccess { lista ->
                    _registros.value = lista
                }
                .onFailure { error ->
                    _errorMessage.value = "Error al cargar registros: ${error.message}"
                    _registros.value = emptyList()
                }

            _isLoading.value = false
        }
    }

    /**
     * Filtra registros por nombre de mascota.
     */
    fun filtrarPorMascota(nombre: String) {
        viewModelScope.launch {
            atencionRepository.obtenerTodasLasAtenciones()
                .onSuccess { lista ->
                    _registros.value = lista.filter {
                        it.mascota.nombre.contains(nombre, ignoreCase = true)
                    }
                }
        }
    }

    /**
     * Obtiene estadísticas del historial.
     */
    fun obtenerEstadisticas(): Triple<Int, Int, String> {
        val lista = _registros.value.orEmpty()
        return Triple(
            lista.map { it.mascota.nombre }.distinct().size, // Total mascotas únicas
            lista.size, // Total consultas
            lista.lastOrNull()?.dueno?.nombre ?: "N/A" // Último dueño
        )
    }
}