package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.UiPost
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

data class SocialUiState(
    val posts: List<UiPost> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SocialViewModel(
    private val postRepository: PostRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SocialUiState())
    val uiState: StateFlow<SocialUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // --- ¡CORRECCIÓN! ---
                // Accedemos a la propiedad 'userId' (un Flow), no a una función getUserId().
                val currentUserId = userPreferencesRepository.userId.firstOrNull()

                val loadedPosts = postRepository.getPosts(currentUserId)
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, posts = loadedPosts)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, errorMessage = "Error al cargar los posts: ${e.message}")
                }
            }
        }
    }

    fun onLikeClicked(postId: Long) {
        viewModelScope.launch {

            val currentUserId = userPreferencesRepository.userId.firstOrNull()
            if (currentUserId == null) {
                return@launch
            }

            _uiState.update { currentState ->
                val updatedPosts = currentState.posts.map { post ->
                    if (post.id == postId) {
                        post.copy(
                            isLiked = !post.isLiked,
                            likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
                        )
                    } else {
                        post
                    }
                }
                currentState.copy(posts = updatedPosts)
            }

            try {
                postRepository.toggleLike(postId, currentUserId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun deletePost(postId: Long) {
        viewModelScope.launch {
            try {
                // Llamamos a la API para que borre el post.
                postRepository.deletePost(postId)

                // Si la llamada es exitosa, actualizamos la UI.
                _uiState.update { currentState ->
                    val updatedPosts = currentState.posts.filter { it.id != postId }
                    currentState.copy(posts = updatedPosts)
                }
            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(errorMessage = "Error al borrar el post: ${e.message}")
                }
                e.printStackTrace()
            }
        }
    }

}




class SocialViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocialViewModel::class.java)) {
            val apiService: ApiService = RetrofitProvider.api
            val postRepository = PostRepositoryImpl(apiService, application)
            val userPreferencesRepository = UserPreferencesRepository.getInstance(application)
            return SocialViewModel(postRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
