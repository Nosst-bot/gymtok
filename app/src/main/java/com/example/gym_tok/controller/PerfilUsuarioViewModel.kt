package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.model.UsuarioLocal
import com.example.gym_tok.repository.UserPreferencesRepository
import com.example.gym_tok.repository.UsuarioLocalRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// El estado de la UI vuelve a tener el expediente completo.
data class ProfileUiState(
    val user: UsuarioLocal? = null,
    val isLoading: Boolean = true,
    val isLoggedOut: Boolean = false
)

class PerfilUsuarioViewModel(
    application: Application,
    private val usuarioLocalRepository: UsuarioLocalRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Usamos flatMapLatest para una cadena reactiva elegante.
            // 1. Escucha el ID de usuario de DataStore.
            userPreferencesRepository.userId
                .flatMapLatest { userId ->
                    // 2. Si el ID existe, cambia a escuchar el Flow de Room.
                    if (userId != null) {
                        usuarioLocalRepository.getUserById(userId)
                    } else {
                        // Si no hay ID, emite un flow nulo.
                        flowOf(null)
                    }
                }
                // 3. Recoge el resultado (el UsuarioLocal de Room) y actualiza la UI.
                .collect { userFromDb ->
                    _uiState.update {
                        it.copy(user = userFromDb, isLoading = false)
                    }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clear()
            _uiState.update { it.copy(isLoggedOut = true) }
        }
    }
}