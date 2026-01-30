package com.duoc.veterinaria.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.local.db.VeterinariaDatabase
import com.duoc.veterinaria.data.local.prefs.ClientesPrefs
import com.duoc.veterinaria.data.local.sqlite.ClientesLogDbHelper
import com.duoc.veterinaria.data.repository.ClientePersistenciaRepository
import com.duoc.veterinaria.data.model.Cliente
import com.duoc.veterinaria.utils.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientesViewModel(app: Application) : AndroidViewModel(app) {

    private val TAG = AppLogger.Tags.VIEWMODEL

    private val db = VeterinariaDatabase.getInstance(app)

    private val repo = ClientePersistenciaRepository(
        dao = db.clienteDao(),
        prefs = ClientesPrefs(app),
        logs = ClientesLogDbHelper(app)
    )

    // Estados de carga
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    // Estado de errores
    private val _errorState = MutableLiveData<String?>()
    val errorState: LiveData<String?> = _errorState

    // Lista completa de clientes
    private val _todosClientes = repo.clientesFlow().asLiveData().map { lista ->
        AppLogger.d(TAG, "Clientes cargados desde Flow: ${lista.size} registros")
        lista.map { Cliente(it.nombre, it.email, it.telefono) }
    }

    // Lista filtrada que se muestra en la UI
    private val _clientesFiltrados = MutableLiveData<List<Cliente>>()
    val clientesFiltrados: LiveData<List<Cliente>> = _clientesFiltrados

    // Término de búsqueda actual
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    init {
        AppLogger.i(TAG, "ClientesViewModel inicializado")

        try {
            viewModelScope.launch {
                _todosClientes.observeForever { clientes ->
                    try {
                        aplicarFiltro(clientes, _searchQuery.value ?: "")
                    } catch (e: Exception) {
                        AppLogger.e(TAG, "Error al aplicar filtro inicial", e)
                        _errorState.postValue("Error al cargar clientes: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error crítico en init block", e)
            _errorState.postValue("Error al inicializar: ${e.message}")
        }
    }

    fun obtenerUltimoEmail(): String {
        AppLogger.methodEntry(TAG, "obtenerUltimoEmail")

        return try {
            val email = ClientesPrefs(getApplication()).getUltimoEmail()
            AppLogger.methodExit(TAG, "obtenerUltimoEmail", "email=$email")
            email
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error al obtener último email", e)
            _errorState.postValue("Error al recuperar email guardado")
            ""
        }
    }

    fun registrarCliente(nombre: String, email: String, telefono: String) {
        AppLogger.methodEntry(TAG, "registrarCliente", "nombre=$nombre, email=$email")

        val startTime = System.currentTimeMillis()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _isLoading.postValue(true)
                _errorState.postValue(null)

                AppLogger.d(TAG, "Iniciando registro de cliente en base de datos")

                // Validación de datos
                if (nombre.isBlank()) {
                    throw IllegalArgumentException("El nombre no puede estar vacío")
                }
                if (email.isBlank()) {
                    throw IllegalArgumentException("El email no puede estar vacío")
                }
                if (telefono.isBlank()) {
                    throw IllegalArgumentException("El teléfono no puede estar vacío")
                }

                AppLogger.d(TAG, "Validación de datos completada exitosamente")

                // Simular operación pesada
                delay(800)

                // Intentar registro
                val resultado = repo.registrarCliente(nombre, email, telefono)

                val duration = System.currentTimeMillis() - startTime
                AppLogger.performance(TAG, "Registro de cliente", duration)

                if (resultado) {
                    AppLogger.i(TAG, "Cliente registrado exitosamente: $email")
                    withContext(Dispatchers.Main) {
                        _errorState.value = null
                    }
                } else {
                    throw Exception("El repositorio retornó false en el registro")
                }

            } catch (e: IllegalArgumentException) {
                AppLogger.w(TAG, "Validación fallida: ${e.message}", e)
                _errorState.postValue("Datos inválidos: ${e.message}")
            } catch (e: Exception) {
                AppLogger.e(TAG, "Error al registrar cliente", e)
                _errorState.postValue("Error al guardar: ${e.message}")
            } finally {
                _isLoading.postValue(false)
                AppLogger.methodExit(TAG, "registrarCliente")
            }
        }
    }

    fun buscarClientes(query: String) {
        AppLogger.methodEntry(TAG, "buscarClientes", "query=$query")

        _searchQuery.value = query

        viewModelScope.launch(Dispatchers.Default) {
            try {
                _isSearching.postValue(true)
                _errorState.postValue(null)

                val startTime = System.currentTimeMillis()

                AppLogger.d(TAG, "Iniciando búsqueda con query: '$query'")

                // Simular procesamiento pesado de búsqueda
                delay(500)

                val clientesActuales = _todosClientes.value ?: emptyList()

                if (clientesActuales.isEmpty()) {
                    AppLogger.w(TAG, "No hay clientes disponibles para buscar")
                }

                aplicarFiltro(clientesActuales, query)

                val duration = System.currentTimeMillis() - startTime
                AppLogger.performance(TAG, "Búsqueda de clientes", duration)

            } catch (e: Exception) {
                AppLogger.e(TAG, "Error durante la búsqueda", e)
                _errorState.postValue("Error en búsqueda: ${e.message}")
            } finally {
                _isSearching.postValue(false)
                AppLogger.methodExit(TAG, "buscarClientes")
            }
        }
    }

    private fun aplicarFiltro(clientes: List<Cliente>, query: String) {
        AppLogger.d(TAG, "Aplicando filtro con ${clientes.size} clientes")

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val resultado = if (query.isBlank()) {
                    AppLogger.d(TAG, "Query vacío, mostrando todos los clientes")
                    clientes
                } else {
                    AppLogger.d(TAG, "Filtrando por query: '$query'")
                    clientes.filter { cliente ->
                        cliente.nombre.contains(query, ignoreCase = true) ||
                                cliente.email.contains(query, ignoreCase = true) ||
                                cliente.telefono.contains(query, ignoreCase = true)
                    }
                }

                AppLogger.i(TAG, "Filtro aplicado: ${resultado.size} resultados de ${clientes.size} totales")

                withContext(Dispatchers.Main) {
                    _clientesFiltrados.value = resultado
                }
            } catch (e: Exception) {
                AppLogger.e(TAG, "Error al aplicar filtro", e)
                _errorState.postValue("Error al filtrar: ${e.message}")
            }
        }
    }

    fun limpiarBusqueda() {
        AppLogger.methodEntry(TAG, "limpiarBusqueda")

        try {
            _searchQuery.value = ""
            _clientesFiltrados.value = _todosClientes.value ?: emptyList()
            _errorState.value = null

            AppLogger.i(TAG, "Búsqueda limpiada exitosamente")
        } catch (e: Exception) {
            AppLogger.e(TAG, "Error al limpiar búsqueda", e)
            _errorState.postValue("Error al limpiar búsqueda")
        }

        AppLogger.methodExit(TAG, "limpiarBusqueda")
    }

    fun clearError() {
        _errorState.value = null
    }

    override fun onCleared() {
        super.onCleared()
        AppLogger.i(TAG, "ClientesViewModel destruido")
    }
}