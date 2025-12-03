package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.* // Importamos todos los modelos necesarios
import com.example.gym_tok.network.LoginRequest
import com.example.gym_tok.network.RegisterRequest
import com.example.gym_tok.network.RetrofitProvider
import com.example.gym_tok.repository.UserPreferencesRepository
import com.example.gym_tok.repository.UsuarioLocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationUiState(
    val isRegistrationSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class FormularioRegistroViewModel(
    application: Application,
    private val usuarioLocalRepository: UsuarioLocalRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val apiService = RetrofitProvider.api
    private val userPreferencesRepository = UserPreferencesRepository.getInstance(application)

    fun registerUser(user: User) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // 1. Preparamos la petición de REGISTRO
                val registerRequest = RegisterRequest(
                    name = user.name,
                    lastName = user.lastName,
                    userName = user.userName,
                    email = user.email,
                    sex = user.sex,
                    birthDate = user.birthDate,
                    password = user.password
                )

                // 2. Ejecutamos el REGISTRO
                val registerResponse = apiService.register(registerRequest)

                if (registerResponse.isSuccessful) {
                    // 3. El registro fue exitoso. Ahora preparamos la petición de LOGIN.
                    val loginRequest = LoginRequest(email = user.email, password = user.password)

                    // 4. Ejecutamos el LOGIN para obtener los datos del usuario (incluido el ID)
                    val loginResponse = apiService.login(loginRequest)

                    if (loginResponse.isSuccessful) {
                        val userDto = loginResponse.body()
                        if (userDto != null) {
                            // 5. ¡Éxito! Tenemos el UserDTO. Guardamos la sesión en DataStore.
                            userPreferencesRepository.saveUserSession(userDto)

                            // 6. Creamos el expediente local para guardarlo en la base de datos Room.
                            val usuarioLocal = UsuarioLocal(
                                id = userDto.id, // Obtenemos el ID del login
                                name = user.name,
                                lastName = user.lastName,
                                email = user.email,
                                birthDate = user.birthDate,
                                sex = user.sex,
                                userName = user.userName

                            )
                            // 7. Usamos la función correcta 'insert' para guardar en Room.
                            usuarioLocalRepository.insert(usuarioLocal)

                            // 8. Actualizamos la UI para navegar a la pantalla de login.
                            _uiState.update {
                                it.copy(isRegistrationSuccessful = true, isLoading = false)
                            }
                        } else {
                            _uiState.update { it.copy(errorMessage = "Login después de registro falló: respuesta vacía.", isLoading = false) }
                        }
                    } else {
                        val errorBody = loginResponse.errorBody()?.string()
                        _uiState.update { it.copy(errorMessage = "Error en login post-registro: $errorBody", isLoading = false) }
                    }
                } else {
                    val errorBody = registerResponse.errorBody()?.string()
                    _uiState.update {
                        it.copy(errorMessage = "Error en el registro: $errorBody", isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Error de conexión: ${e.message}", isLoading = false)
                }
            }
        }
    }
}

class FormularioRegistroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormularioRegistroViewModel::class.java)) {
            // --- ¡CORRECCIÓN! ---
            val database = AppDatabase.get(application)
            val repository = UsuarioLocalRepository(database)
            return FormularioRegistroViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}