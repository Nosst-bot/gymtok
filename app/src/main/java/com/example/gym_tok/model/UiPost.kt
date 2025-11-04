package com.example.gym_tok.model

// Un "data class" es una clase especial de Kotlin que guarda datos de forma simple.
// No tiene lógica de negocio, solo almacena la información que la interfaz necesita mostrar.

data class UiPost(
    val id: Int,          // Identificador único del post (sirve como "key" en la lista LazyColumn)
    val user: String,     // Nombre del usuario que publicó
    val time: String,     // Cuándo se publicó (ej: "Hace 2h", "Ayer")
    val text: String? = null, // Texto opcional del post
    val imageUrl: String? = null // Imagen opcional (por ahora la dejaremos nula)
)
