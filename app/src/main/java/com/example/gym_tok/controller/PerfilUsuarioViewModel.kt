package com.example.gym_tok.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.db.AppDatabase
import com.example.gym_tok.model.UsuarioLocal
import com.example.gym_tok.repository.UsuarioLocalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilUsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioLocalRepository(AppDatabase.get(application))

    val loggedInUser: StateFlow<UsuarioLocal?> = repository.getLoggedInUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
