package com.example.gym_tok.controller

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.DTO.CommentRequest
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.Comment
import com.example.gym_tok.network.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ComentariosViewModel (): ViewModel() {

    // 2. CORRECCIÓN: Se inicializa la instancia de ApiService
    private val apiService = RetrofitProvider.api


    // --- ESTADOS DE LA UI ---

    // Estado para la lista de comentarios. Es privado para que solo el ViewModel lo modifique.
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    // Versión pública y de solo lectura del estado para que la UI lo observe.
    val comments: StateFlow<List<Comment>> = _comments

    // Estado para el texto que el usuario está escribiendo en el campo de nuevo comentario.
    private val _newCommentText = MutableStateFlow("")
    val newCommentText: StateFlow<String> = _newCommentText

    // Estado para controlar si se está cargando algo (ej: un spinner de carga).
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado para manejar y mostrar errores.
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // --- LÓGICA DE NEGOCIO ---

    /**
     * Carga la lista de comentarios para un post específico desde la API.
     */
    fun loadComments(postId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getComments(postId)
                if (response.isSuccessful) {
                    _comments.value = response.body() ?: emptyList()
                } else {
                    _error.value = "Error al cargar los comentarios: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al cargar comentarios: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Envía un nuevo comentario a la API y actualiza la lista.
     */
    fun createComment(postId: Long, userName: String) {
        // Evita enviar comentarios vacíos
        if (newCommentText.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            val request = CommentRequest(
                text = newCommentText.value,
                userName = userName
            )
            try {
                val response = apiService.createComment(postId, request)
                if (response.isSuccessful) {
                    // Si el comentario se crea con éxito, limpiamos el campo de texto
                    _newCommentText.value = ""
                    // Y recargamos la lista para mostrar el nuevo comentario inmediatamente.
                    loadComments(postId)
                } else {
                    _error.value = "Error al crear el comentario: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al crear comentario: ${e.message}"
            }
        }
    }

    /**
     * Función que la UI llamará cada vez que el texto del campo de comentario cambie.
     */
    fun onNewCommentTextChanged(text: String) {
        _newCommentText.value = text
    }

}