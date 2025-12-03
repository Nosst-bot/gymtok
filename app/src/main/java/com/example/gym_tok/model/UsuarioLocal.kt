package com.example.gym_tok.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("usuarioLocal")
data class UsuarioLocal (
    val id: Int,
    val name: String,
    val lastName: String,
    @PrimaryKey
    val email: String,
    val birthDate: String,
    val sex: Char,
    val userName: String,
    val isLoggedIn: Boolean = false
)