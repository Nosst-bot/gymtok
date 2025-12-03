package com.example.gym_tok.network

import com.google.gson.annotations.SerializedName

/**
 * Representa la estructura de un Post tal como llega desde el backend.
 * Esta es la "plantilla" que usa Retrofit para entender el JSON.
 */
data class PostResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("description")
    val description: String?,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("createdAt")
    val createdAt: String,

    @SerializedName("userName")
    val userName: String?,


    @SerializedName("likesCount")
    val likesCount: Int?,

    @SerializedName("isLikedByCurrentUser")
    val isLikedByCurrentUser: Boolean?
)
