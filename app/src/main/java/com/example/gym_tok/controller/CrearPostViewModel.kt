package com.example.gym_tok.controller

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.network.RetrofitProvider
import com.example.gym_tok.repository.PostRepository
import com.example.gym_tok.repository.PostRepositoryImpl
import com.example.gym_tok.repository.UserPreferencesRepository // <-- 1. IMPORTAR
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CrearPostUiState(
    val postText: String = "",
    val imageUri: Uri? = null,
    val isPublishing: Boolean = false,
    val postCreatedSuccessfully: Boolean = false
)

// 2. AÑADIMOS el nuevo repositorio al constructor.
class CrearPostViewModel(
    application: Application,
    private val postRepository: PostRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CrearPostUiState())
    val uiState: StateFlow<CrearPostUiState> = _uiState.asStateFlow()

    fun onPostTextChanged(newText: String) {
        _uiState.update { it.copy(postText = newText) }
    }

    fun onImageUriReceived(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun publishPost() {
        val currentState = _uiState.value
        if (currentState.isPublishing || (currentState.postText.isBlank() && currentState.imageUri == null)) {
            return
        }

        viewModelScope.launch {
            // 3. OBTENEMOS EL USUARIO DESDE EL REPOSITORIO DE PREFERENCIAS.
            // .first() obtiene el valor actual del Flow.
            val userName = userPreferencesRepository.userName.first()

            // Si no hay usuario logueado, no hacemos nada.
            if (userName == null) {
                // Aquí podrías emitir un error a la UI si quisieras.
                return@launch
            }

            _uiState.update { it.copy(isPublishing = true) }

            val success = postRepository.createPost(
                description = currentState.postText,
                imageUri = currentState.imageUri,
                userName = userName // <-- 4. USAMOS EL VALOR REAL DE LA SESIÓN.
            )

            if (success) {
                _uiState.update { it.copy(isPublishing = false, postCreatedSuccessfully = true) }
            } else {
                _uiState.update { it.copy(isPublishing = false) }
            }
        }
    }

    fun onPostPublished() {
        _uiState.update { it.copy(postCreatedSuccessfully = false) }
    }
}

// 5. ACTUALIZAMOS LA FACTORY para que proporcione el nuevo repositorio.
class CrearPostViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearPostViewModel::class.java)) {
            val apiService: ApiService = RetrofitProvider.api
            val postRepository = PostRepositoryImpl(apiService, application)
            // Forma correcta de obtener la instancia del Singleton
            val userPrefsRepository = UserPreferencesRepository.getInstance(application)
            return CrearPostViewModel(application, postRepository, userPrefsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}