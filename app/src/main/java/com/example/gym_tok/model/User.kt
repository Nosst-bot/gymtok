package com.example.gym_tok.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val sex: Char,
    val userName: String,
    val password: String
) {
    fun toUsuarioLocal(): UsuarioLocal {
        return UsuarioLocal(
            id = this.id,
            name = this.name,
            lastName = this.lastName,
            email = this.email,
            birthDate = this.birthDate,
            sex = this.sex,
            userName = this.userName,
            password = this.password
        )
    }
}
