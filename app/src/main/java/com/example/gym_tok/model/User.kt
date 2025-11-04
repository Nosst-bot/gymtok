package com.example.gym_tok.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val sex: Char,
    val userName: String,
    val password: String
)