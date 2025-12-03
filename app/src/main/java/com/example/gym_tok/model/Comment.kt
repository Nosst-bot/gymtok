package com.example.gym_tok.model

data class Comment (
    val id: Long,
    val text: String,
    val userName: String,
    val createdAt: String // El backend enviará el 'Instant' como un String en formato ISO (ej: "2023-10-27T10:15:30Z")
)