package com.example.gym_tok.network

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String, // Se envía la fecha como String en formato estándar ISO (ej: "1990-12-25")
    val sex: Char,
    val userName: String,
    val password: String
)
