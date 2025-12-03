package com.example.gym_tok.repository

import android.net.Uri
import com.example.gym_tok.model.UiPost

/**
 * El contrato que define las acciones que se pueden realizar con los posts.
 */
interface PostRepository {

    /**
     * Obtiene la lista de posts.
     * @param userId El ID del usuario actual, para que el backend pueda calcular
     * si a este usuario le gusta cada post. Puede ser nulo.
     * @return Una lista de [UiPost] lista para ser mostrada en la UI.
     */
    suspend fun getPosts(userId: Int?): List<UiPost>

    /**
     * Crea un nuevo post.
     */
    suspend fun createPost(description: String, imageUri: Uri?, userName: String): Boolean

    /**
     * Envía la acción de dar o quitar "me gusta" a un post.
     * @param postId El ID del post que recibe el like.
     * @param userId El ID del usuario que da el like.
     */
    suspend fun toggleLike(postId: Long, userId: Int)

    suspend fun deletePost(postId: Long)

    suspend fun getPostById(postId: Long, userId: Int?): UiPost?

    // En PostRepository.kt

    suspend fun updatePost(postId: Long, description: String, deleteImage: Boolean)

}
