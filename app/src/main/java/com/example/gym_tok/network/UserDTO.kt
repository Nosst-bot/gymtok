package com.example.gym_tok.network

import com.example.gym_tok.model.UsuarioLocal

/**
 * Data class representing the user information received from the backend upon successful login.
 * It intentionally omits sensitive data like the password.
 */
data class UserDTO(
    val id: Int,
    val name: String,
    val lastName: String,
    val userName: String,
    val email: String,
    val birthDate: String,
    val sex: Char

) {
    /**
     * Converts this Data Transfer Object (DTO) into a local database entity (UsuarioLocal).
     * This is used to store the user's profile information locally after a successful login.
     * Fields not provided by the login response are set to default empty values.
     */
    fun toUsuarioLocal(): UsuarioLocal {
        return UsuarioLocal(
            id = this.id,
            name = this.name,
            userName = this.userName,
            email = this.email,
            lastName = this.lastName,
            birthDate = this.birthDate, // Not provided by login DTO
            sex = this.sex // Not provided by login DT
        )
    }
}
