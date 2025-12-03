package com.example.gym_tok.model

/**
 * Data class representing the user information received from the backend upon successful login.
 * It intentionally omits sensitive data like the password.
 */
data class UserDTO(
    val id: Int,
    val name: String,
    val userName: String,
    val email: String
)
