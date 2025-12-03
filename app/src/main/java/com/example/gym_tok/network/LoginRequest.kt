package com.example.gym_tok.network

/**
 * Data class representing the JSON body for a login request.
 * The field names ('email', 'password') must exactly match the JSON keys expected by the backend.
 */
data class LoginRequest(
    val email: String,
    val password: String
)