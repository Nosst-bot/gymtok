package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.repository.UsuarioLocalRepository

class FormularioRegistroViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormularioRegistroViewModel::class.java)) {
            // --- ¡CORRECCIÓN! ---
            val database = AppDatabase.get(application)
            val repository = UsuarioLocalRepository(database)
            return FormularioRegistroViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}