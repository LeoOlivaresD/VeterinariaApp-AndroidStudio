package com.duoc.veterinaria.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//Este Factory permite que Jetpack Compose instancie correctamente
//el ViewModel con el contexto de Application.
class ClientesViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClientesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClientesViewModel(app) as T
        }
        throw IllegalArgumentException("ViewModel no soportado")
    }
}