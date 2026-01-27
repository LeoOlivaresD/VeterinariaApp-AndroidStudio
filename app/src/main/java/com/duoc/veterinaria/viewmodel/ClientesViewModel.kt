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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClientesViewModel(app: Application) : AndroidViewModel(app) {

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

    // Lista completa de clientes
    private val _todosClientes = repo.clientesFlow().asLiveData().map { lista ->
        lista.map { Cliente(it.nombre, it.email, it.telefono) }
    }

    // Lista filtrada que se muestra en la UI
    private val _clientesFiltrados = MutableLiveData<List<Cliente>>()
    val clientesFiltrados: LiveData<List<Cliente>> = _clientesFiltrados

    // Término de búsqueda actual
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    init {
        // Observar cambios en la lista completa y aplicar filtro
        viewModelScope.launch {
            _todosClientes.observeForever { clientes ->
                aplicarFiltro(clientes, _searchQuery.value ?: "")
            }
        }
    }

    fun obtenerUltimoEmail(): String = ClientesPrefs(getApplication()).getUltimoEmail()

    fun registrarCliente(nombre: String, email: String, telefono: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)

            // Simular operación pesada
            delay(800)

            repo.registrarCliente(nombre, email, telefono)

            _isLoading.postValue(false)
        }
    }

    // Búsqueda asincrónica con simulación de operación pesada
    fun buscarClientes(query: String) {
        _searchQuery.value = query

        viewModelScope.launch(Dispatchers.Default) {
            _isSearching.postValue(true)

            // Simular procesamiento pesado de búsqueda
            delay(500)

            val clientesActuales = _todosClientes.value ?: emptyList()
            aplicarFiltro(clientesActuales, query)

            _isSearching.postValue(false)
        }
    }

    private fun aplicarFiltro(clientes: List<Cliente>, query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            val resultado = if (query.isBlank()) {
                clientes
            } else {
                clientes.filter { cliente ->
                    cliente.nombre.contains(query, ignoreCase = true) ||
                            cliente.email.contains(query, ignoreCase = true) ||
                            cliente.telefono.contains(query, ignoreCase = true)
                }
            }

            withContext(Dispatchers.Main) {
                _clientesFiltrados.value = resultado
            }
        }
    }

    // Limpiar búsqueda
    fun limpiarBusqueda() {
        _searchQuery.value = ""
        _clientesFiltrados.value = _todosClientes.value ?: emptyList()
    }
}