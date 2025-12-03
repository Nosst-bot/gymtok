package com.example.gym_tok.model
/**
 * Data Transfer Object (DTO) para enviar la información de registro al backend.
 * Este es el "contrato" que el frontend debe cumplir para el endpoint /user/register.
 * Su estructura es un espejo del RegisterRequest.java del backend.
 */
data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String, // Enviamos la fecha como String (ej: "25-12-1990")
    val sex: Char,
    val userName: String,
    val password: String
)
