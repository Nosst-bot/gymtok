package com.example.gym_tok.model

// Este es el objeto que la UI (la Vista) realmente usa.
data class UiPost(
    val id: Long,
    val user: String, // Por ahora será un placeholder
    val time: String, // El tiempo formateado, ej: "hace 5 min"
    val text: String?, // La descripción del post
    val imageUrl: String?, // La URL de la imagen del post
    val likesCount: Int,
    val isLiked: Boolean
)
