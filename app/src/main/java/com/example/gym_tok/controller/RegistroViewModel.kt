package com.example.gym_tok.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class RegistroUiState(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val lastName: String = "",
    val userName: String = "",
    val birthDate: String = "",
    val sex: Char = 'M',
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val registradoOk: Boolean = false
)

class RegistroViewModel(): ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }

    private val _state = MutableStateFlow(RegistroUiState())
    val state: StateFlow<RegistroUiState> = _state.asStateFlow()

    fun onEmailChange(v: String) = _state.update { it.copy(email = v, errorMessage = null) }
    fun onPasswordChange(v: String) = _state.update { it.copy(password = v, errorMessage = null) }
    fun onNameChange(v: String) = _state.update { it.copy(name = v, errorMessage = null) }
    fun onLastNameChange(v: String) = _state.update { it.copy(lastName = v, errorMessage = null) }
    fun onUserNameChange(v: String) = _state.update { it.copy(userName = v, errorMessage = null) }
    fun onSexChange(v: Char) = _state.update { it.copy(sex = v, errorMessage = null) }
    fun onBirthDateChange(v: String) = _state.update { it.copy(birthDate = v, errorMessage = null) }
    fun limpiarError() = _state.update { it.copy(errorMessage = null) }

    fun register(nav: NavController) {
        val email = state.value.email.trim()
        val pass = state.value.password
        val name = state.value.name.trim()
        val lastName = state.value.lastName.trim()
        val userName = state.value.userName.trim()
        val sex = state.value.sex
        val birthDate = state.value.birthDate

        // Validaciones básicas (didácticas)
        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || lastName.isEmpty() || userName.isEmpty() || birthDate.isEmpty()) {
            _state.update { it.copy(errorMessage = "Completa todos los campos") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _state.update { it.copy(errorMessage = "Email no válido") }
            return
        }
        if (pass.length < 5) {
            _state.update { it.copy(errorMessage = "La contraseña debe tener al menos 5 caracteres") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null, registradoOk = false) }
            delay(3000)
            try {
                val nuevoUser = User(name = name, email = email, lastName = lastName, password = pass, birthDate = birthDate, userName = userName, sex = sex)
                api.register(nuevoUser)

                _state.update {
                    it.copy(
                        isSaving = false,
                        registradoOk = true,
                        password = "",
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, errorMessage = e.message ?: "Error al registrar") }
            }
        }
    }
}