package com.example.gym_tok.repository

import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.UsuarioLocal
import kotlinx.coroutines.flow.Flow

class UsuarioLocalRepository(private val db: AppDatabase) {
    private val usuarioLocalDAO = db.usuarioLocalDAO()

    suspend fun insert(u: UsuarioLocal) = usuarioLocalDAO.insert(u)

    fun getLoggedInUser(): Flow<UsuarioLocal?> = usuarioLocalDAO.getLoggedInUser()

    suspend fun login(email: String, pass: String): UsuarioLocal? {
        val user = usuarioLocalDAO.getUserByEmail(email)
        if (user != null && user.password == pass) {
            usuarioLocalDAO.logoutAll()
            usuarioLocalDAO.loginUser(user.id as Int)
            return user
        }
        return null
    }

    suspend fun logout() {
        usuarioLocalDAO.logoutAll()
    }
}
