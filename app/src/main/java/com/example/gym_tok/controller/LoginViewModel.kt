package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.User
import com.example.gym_tok.repository.UsuarioLocalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.EOFException

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val usuarioLogeado: User? = null
)

class LoginViewModel(application: Application): AndroidViewModel(application) {
    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }
    private val localRepository = UsuarioLocalRepository(AppDatabase.get(application))
    private val _state = MutableStateFlow(LoginUiState());
    val state: StateFlow<LoginUiState> = _state.asStateFlow();

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, errorMessage = null) }
    }

    fun limpiarError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun login(nav: NavController) {
        val email = state.value.email.trim()
        val pass = state.value.password

        if (email.isEmpty() || pass.isEmpty()) {
            _state.update { it.copy(errorMessage = "Ingresa email y contraseña") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // First, try to log in against the local database
                var user = localRepository.login(email, pass)

                // If not found locally, try remote API
                if (user == null) {
                    val remoteUser = api.login(email, pass)
                    if(remoteUser != null) {
                        // If login is successful, save user to local DB and then log in
                        val localUser = remoteUser.toUsuarioLocal()
                        localRepository.insert(localUser)
                        user = localRepository.login(email, pass)
                    }
                }

                if (user != null) {
                    delay(2000)
                    _state.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                    nav.navigate("home")
                } else {
                    _state.update { it.copy(isLoading = false, errorMessage = "Credenciales inválidas") }
                }
            } catch (e: EOFException) {
                _state.update { it.copy(isLoading = false, errorMessage = "Credenciales inválidas") }
            }
            catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message ?: "Error inesperado") }
            }
        }
    }
}
