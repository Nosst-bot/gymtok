package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.*
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.network.RetrofitProvider
import com.example.gym_tok.repository.PostRepository
import com.example.gym_tok.repository.PostRepositoryImpl
import com.example.gym_tok.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditPostUiState(
    val postId: Long = 0L,
    var description: String = "",
    var imageUrl: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val navigateBack: Boolean = false // Para señalar cuándo cerrar la pantalla
)

class EditPostViewModel(
    private val postId: Long,
    private val postRepository: PostRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditPostUiState())
    val uiState: StateFlow<EditPostUiState> = _uiState.asStateFlow()

    private var initialImageUrl: String? = null

    init {
        loadPost()
    }

    private fun loadPost() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val userId = userPreferencesRepository.userId.firstOrNull()
                val post = postRepository.getPostById(postId, userId)

                if(post != null) {
                    initialImageUrl = post.imageUrl
                    _uiState.update {
                        it.copy(
                            postId = post.id,
                            description = post.text ?: "",
                            imageUrl = post.imageUrl,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "Post no encontrado") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar el post: ${e.message}") }
            }
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onDeleteImage() {
        _uiState.update { it.copy(imageUrl = null) }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val currentState = uiState.value
            // Determinamos si el usuario borró la imagen.
            val deleteImage = initialImageUrl != null && currentState.imageUrl == null

            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Llamamos al repositorio para que guarde los cambios.
                postRepository.updatePost(
                    postId = currentState.postId,
                    description = currentState.description,
                    deleteImage = deleteImage
                )
                // Si todo va bien, indicamos a la vista que navegue hacia atrás.
                _uiState.update { it.copy(isLoading = false, navigateBack = true) }
            } catch (e: Exception) {
                // Si hay un error, lo mostramos.
                _uiState.update { it.copy(isLoading = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }
}


class EditPostViewModelFactory(
    private val application: Application,
    private val postId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditPostViewModel::class.java)) {
            val apiService: ApiService = RetrofitProvider.api
            val postRepository = PostRepositoryImpl(apiService, application)
            val userPreferencesRepository = UserPreferencesRepository.getInstance(application)
            return EditPostViewModel(postId, postRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}