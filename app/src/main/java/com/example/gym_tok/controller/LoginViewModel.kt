package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.User
import com.example.gym_tok.network.LoginRequest
import com.example.gym_tok.network.RetrofitProvider
import com.example.gym_tok.repository.UserPreferencesRepository
import com.example.gym_tok.repository.UsuarioLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val errorMessage: String? = null
)

class LoginViewModel(
    application: Application,
    private val apiService: ApiService,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val usuarioLocalRepository: UsuarioLocalRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun onPasswordChange(contrasena: String) {
        _uiState.update { it.copy(contrasena = contrasena, errorMessage = null) }
    }

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.contrasena

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email y contraseña no pueden estar vacíos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val loginRequest = LoginRequest(email = email, password = password)
                val response = apiService.login(loginRequest)

                if (response.isSuccessful && response.body() != null) {
                    val userDto = response.body()!!

                    // Acción 1: Guardar la sesión (la "llave")
                    userPreferencesRepository.saveUserSession(userDto)

                    // Acción 2: Guardar el perfil completo en la base de datos local
                    usuarioLocalRepository.insert(userDto.toUsuarioLocal())

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                } else {
                    val errorMsg = "Error (${response.code()}): Credenciales incorrectas o problema del servidor."
                    _uiState.update { it.copy(isLoading = false, errorMessage = errorMsg) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error de red: ${e.message}") }
            }
        }
    }

    fun onNavigateDone(){
        _uiState.update { it.copy(loginSuccess = false) }
    }
}

class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val apiService = RetrofitProvider.api
            val userPrefsRepository = UserPreferencesRepository.getInstance(application)
            
            val database = AppDatabase.get(application)
            val usuarioLocalRepository = UsuarioLocalRepository(database)

            return LoginViewModel(application, apiService, userPrefsRepository, usuarioLocalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
