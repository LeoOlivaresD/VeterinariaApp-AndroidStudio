package com.duoc.veterinaria.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la autenticación de usuarios.
 *
 * Usuarios simulados sin persistencia (en memoria).
 */
class AuthViewModel : ViewModel() {

    // Estado de autenticación
    private val _isAuthenticated = MutableLiveData(false)
    val isAuthenticated: LiveData<Boolean> = _isAuthenticated

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _currentUser = MutableLiveData<Usuario?>()
    val currentUser: LiveData<Usuario?> = _currentUser

    // Usuarios simulados en memoria
    private val usuarios = mutableListOf(
        Usuario("admin", "admin123", "Administrador", "Administrador del Sistema", "admin@veterinaria.cl"),
        Usuario("veterinario", "vet123", "Dr. Veterinario", "Veterinario Principal", "vet@veterinaria.cl"),
        Usuario("asistente", "asist123", "Asistente", "Asistente Veterinario", "asist@veterinaria.cl"),
        Usuario("cliente", "cliente123", "Cliente Demo", "Cliente", "cliente@veterinaria.cl")
    )

    /**
     * Inicia sesión con credenciales.
     */
    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Simular delay de red
            delay(1500)

            val usuario = usuarios.find {
                it.username == username && it.password == password
            }

            if (usuario != null) {
                _currentUser.value = usuario
                _isAuthenticated.value = true
                _isLoading.value = false
                onSuccess()
            } else {
                _errorMessage.value = "Usuario o contraseña incorrectos"
                _isLoading.value = false
            }
        }
    }

    /**
     * Restablece la contraseña de un usuario.
     */
    fun resetPassword(username: String, email: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            delay(1000)

            val usuario = usuarios.find {
                it.username == username && it.email == email
            }

            if (usuario != null) {
                // Generar nueva contraseña temporal
                val nuevaPassword = "temp${(1000..9999).random()}"
                usuario.password = nuevaPassword
                _isLoading.value = false
                onSuccess(nuevaPassword)
            } else {
                _errorMessage.value = "Usuario o email no encontrados"
                _isLoading.value = false
            }
        }
    }

    /**
     * Cierra sesión.
     */
    fun logout() {
        _currentUser.value = null
        _isAuthenticated.value = false
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _errorMessage.value = null
    }
}

/**
 * Modelo de Usuario (sin persistencia).
 */
data class Usuario(
    val username: String,
    var password: String,
    val nombreCompleto: String,
    val rol: String,
    val email: String
)