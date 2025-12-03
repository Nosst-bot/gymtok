package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.repository.UserPreferencesRepository
import com.example.gym_tok.repository.UsuarioLocalRepository

class PerfilUsuarioViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilUsuarioViewModel::class.java)) {
            val database = AppDatabase.get(application)
            // --- ¡AQUÍ ESTÁ LA CORRECCIÓN! ---
            // Le pasamos el objeto 'database' completo, no solo el DAO.
            val usuarioLocalRepository = UsuarioLocalRepository(database)
            val userPreferencesRepository = UserPreferencesRepository.getInstance(application)
            return PerfilUsuarioViewModel(application, usuarioLocalRepository, userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}