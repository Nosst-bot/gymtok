package com.example.gym_tok.repository

import android.content.Context
import android.net.Uri
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.UiPost
import com.example.gym_tok.network.PostResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PostRepositoryImpl(
    private val apiService: ApiService,
    private val context: Context
) : PostRepository {

    // --- 1. MÉTODO ACTUALIZADO ---
    // Ahora acepta el ID del usuario y se lo pasa a la API.
    override suspend fun getPosts(userId: Int?): List<UiPost> {
        try {
            val response = apiService.getPosts(userId) // Se pasa el userId
            if (response.isSuccessful) {
                return response.body()?.map { it.toUiPost() } ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    // --- 2. NUEVO MÉTODO IMPLEMENTADO ---
    // Llama directamente al endpoint de la API para dar/quitar like.
    override suspend fun toggleLike(postId: Long, userId: Int) {
        try {
            // No nos importa la respuesta, solo que la llamada se haga.
            apiService.toggleLike(postId, userId)
        } catch (e: Exception) {
            // Es buena práctica registrar cualquier error de red.
            e.printStackTrace()
        }
    }

    // --- (createPost no tiene cambios) ---
    override suspend fun createPost(description: String, imageUri: Uri?, userName: String): Boolean {
        return try {
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val userNameBody = userName.toRequestBody("text/plain".toMediaTypeOrNull())

            val imagePart: MultipartBody.Part? = imageUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val fileBytes = inputStream.readBytes()
                    val requestFile = fileBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("image", "photo.jpg", requestFile)
                }
            }

            val response = apiService.createPost(
                userName = userNameBody,
                description = descriptionBody,
                image = imagePart
            )

            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    // --- ¡IMPLEMENTACIÓN DE LA NUEVA FUNCIÓN! ---
    override suspend fun deletePost(postId: Long) = withContext(Dispatchers.IO) {
        val response = apiService.deletePost(postId)
        // Si la respuesta no es exitosa (ej: 204 No Content), lanzamos una excepción.
        if (!response.isSuccessful) {
            throw Exception("Error al borrar el post: ${response.code()}")
        }
    }

    override suspend fun getPostById(postId: Long, userId: Int?): UiPost = withContext(Dispatchers.IO) {
        val response = apiService.getPostById(postId, userId)
        if (response.isSuccessful) {
            // Usamos el 'toUiPost()' que ya teníamos para transformar la respuesta.
            response.body()?.toUiPost() ?: throw Exception("El cuerpo de la respuesta del post está vacío")
        } else {
            throw Exception("Error al obtener el post: ${response.code()}")
        }
    }

    // En PostRepositoryImpl.kt

    override suspend fun updatePost(postId: Long, description: String, deleteImage: Boolean) = withContext(Dispatchers.IO) {
        val response = apiService.updatePost(postId, description, deleteImage)
        if (!response.isSuccessful) {
            throw Exception("Error al actualizar el post: ${response.code()}")
        }
    }

}

fun PostResponse.toUiPost(): UiPost {
    val instant = Instant.parse(this.createdAt)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        .withZone(ZoneId.systemDefault())

    // 1. Verificamos si la URL relativa de la imagen (ej: "/uploads/foto.jpg") existe.
    val relativeImageUrl = this.imageUrl
    val fullImageUrl: String? // Esta será la URL completa y final.

    if (relativeImageUrl.isNullOrBlank()) {
        // 2. Si el post no tiene imagen, la URL final es nula.
        fullImageUrl = null
    } else {
        // 3. Si tiene imagen, construimos la URL completa añadiendo la IP del servidor.
        fullImageUrl = ApiService.BASE_URL + relativeImageUrl
    }

    // 4. EL "SOPLÓN": Imprimimos en la consola la URL que la app va a intentar cargar.
    //    Busca la etiqueta "URL_SOPLON" en el Logcat para ver este mensaje.
    android.util.Log.d("URL_SOPLON", "Post ID ${this.id}: URL a cargar -> '$fullImageUrl'")

    return UiPost(
        id = this.id,
        user = this.userName ?: "Usuario",
        time = formatter.format(instant),
        text = this.description,
        imageUrl = fullImageUrl, // Pasamos la URL completa (o null) a la UI.
        likesCount = this.likesCount ?: 0,
        isLiked = this.isLikedByCurrentUser ?: false
    )
}