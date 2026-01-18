package com.duoc.veterinaria.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.duoc.veterinaria.data.local.db.VeterinariaDatabase
import com.duoc.veterinaria.data.local.prefs.ClientesPrefs
import com.duoc.veterinaria.data.local.sqlite.ClientesLogDbHelper
import com.duoc.veterinaria.data.repository.ClienteRepository
import com.duoc.veterinaria.data.model.Cliente
import kotlinx.coroutines.launch

class ClientesViewModel(app: Application) : AndroidViewModel(app) {

    private val db = VeterinariaDatabase.getInstance(app)

    private val repo = ClienteRepository(
        dao = db.clienteDao(),
        prefs = ClientesPrefs(app),
        logs = ClientesLogDbHelper(app)
    )

    val clientes: LiveData<List<Cliente>> =
        repo.clientesFlow().asLiveData().map { lista ->
            lista.map { Cliente(it.nombre, it.email, it.telefono) }
        }

    fun obtenerUltimoEmail(): String = ClientesPrefs(getApplication()).getUltimoEmail()

    fun registrarCliente(nombre: String, email: String, telefono: String) {
        viewModelScope.launch {
            repo.registrarCliente(nombre, email, telefono)
        }
    }
}