package com.example.gym_tok.repository

import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.UsuarioLocal
import kotlinx.coroutines.flow.Flow

class UsuarioLocalRepository(private val db: AppDatabase) {
    private val usuarioLocalDAO = db.usuarioLocalDAO()

    suspend fun insert(u: UsuarioLocal) = usuarioLocalDAO.insert(u)

    fun getUserById(id: Int): Flow<UsuarioLocal?> = usuarioLocalDAO.getUserById(id)

    fun getLoggedInUser(): Flow<UsuarioLocal?> = usuarioLocalDAO.getLoggedInUser()

    suspend fun logout() {
        usuarioLocalDAO.logoutAll()
    }
}
