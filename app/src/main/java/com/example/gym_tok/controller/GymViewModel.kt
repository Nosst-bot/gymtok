package com.example.gym_tok.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gym_tok.api.ApiService
import com.example.gym_tok.model.Gym
import com.example.gym_tok.network.RetrofitProvider
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


    // --- CORRECCIÓN 1: OBTENER LA INSTANCIA DE API ---
    // Ya no se usa un método .create(). RetrofitProvider es un objeto singleton
    // que expone directamente la instancia de ApiService a través de su propiedad `api`.
    private val api: ApiService = RetrofitProvider.api
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
            try {
                // --- CORRECCIÓN 2: LLAMAR AL MÉTODO CORRECTO ---
                // Se usa `getGyms()` como está definido en la interfaz ApiService.
                val response = api.getGyms()

                // --- CORRECCIÓN 3: MANEJO DE RESPUESTA ROBUSTO ---
                // Se verifica si la respuesta de la red fue exitosa (código 2xx).
                if (response.isSuccessful) {
                    // Si es exitosa, se actualiza el estado con la lista de gimnasios del body.
                    // Se usa `?: emptyList()` como salvaguarda por si el body es nulo.
                    _state.update { it.copy(list = response.body() ?: emptyList(), isListLoading = false) }
                } else {
                    // Si la respuesta fue un error (404, 500, etc.), se actualiza el estado con el error.
                    _state.update { it.copy(isListLoading = false, listError = "Error: ${response.code()}") }
                }
            } catch (e: Exception) {
                // Si ocurre una excepción (ej. no hay conexión a internet), se captura y se muestra el error.
                _state.update { it.copy(isListLoading = false, listError = e.message) }
            }
        }
    }

    fun limpiarResultado() {
        _state.update { it.copy(created = null, createError = null) }
    }

}