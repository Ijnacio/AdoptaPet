package cl.aplicacion.adoptapet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.aplicacion.adoptapet.repository.AuthRepository
import cl.aplicacion.adoptapet.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(correo: String, pass: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = repository.login(correo, pass)
            result.onSuccess { usuario ->
                // Guardamos en SessionManager
                SessionManager.login(usuario.nombre, usuario.correo)
                _loginState.value = LoginState.Success
            }.onFailure { error ->
                _loginState.value = LoginState.Error(error.message ?: "Error desconocido")
            }
        }
    }

    fun registro(nombre: String, correo: String, pass: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            val result = repository.registro(nombre, correo, pass)
            result.onSuccess { usuario ->
                SessionManager.login(usuario.nombre, usuario.correo)
                _loginState.value = LoginState.Success
            }.onFailure { error ->
                _loginState.value = LoginState.Error(error.message ?: "Error al registrar")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val mensaje: String) : LoginState()
}