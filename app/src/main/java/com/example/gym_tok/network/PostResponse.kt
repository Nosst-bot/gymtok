package com.example.gym_tok.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa la estructura de un Post tal como llega desde el backend.
 * Esta es la "plantilla" que usa Retrofit para entender el JSON.
 */
@Serializable
data class PostResponse(
    @SerialName("id")
    val id: Long,

    @SerialName("description")
    val description: String?,

    @SerialName("imageUrl")
    val imageUrl: String?,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("userName")
    val userName: String?,


    @SerialName("likesCount")
    val likesCount: Int?,

    @SerialName("isLikedByCurrentUser")
    val isLikedByCurrentUser: Boolean?
)
