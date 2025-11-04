package com.example.gym_tok.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.Gym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GymPostUiState(
    val title: String = "",
    val body: String = "",
    val isCreating: Boolean = false,
    val created: Gym? = null,
    val createError: String? = null,
    val list: List<Gym> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null
)

class GymViewModel: ViewModel() {

    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }
    private val _state = MutableStateFlow(GymPostUiState())
    val state: StateFlow<GymPostUiState> = _state.asStateFlow()

    init {
        cargarGyms()
    }

    fun onTitleChange(value: String) {
        _state.update { it.copy(title = value, createError = null) }
    }

    fun onBodyChange(value: String) {
        _state.update { it.copy(body = value, createError = null) }
    }

    fun cargarGyms() {
        viewModelScope.launch {
            _state.update { it.copy(isListLoading = true, listError = null) }
            flow { emit(api.obtenerGym()) }
                .onEach { gyms -> _state.update { it.copy(list = gyms, isListLoading = false) } }
                .catch { e -> _state.update { it.copy(isListLoading = false, listError = e.message) } }
                .collect()
        }
    }

    fun limpiarResultado() {
        _state.update { it.copy(created = null, createError = null) }
    }

}