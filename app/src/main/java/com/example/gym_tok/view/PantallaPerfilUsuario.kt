package com.example.gym_tok.view

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym_tok.controller.PerfilUsuarioViewModel
import com.example.gym_tok.controller.PerfilUsuarioViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfilUsuario(onCerrarSesion: () -> Unit, onVolver: () -> Unit) {
    // Usamos la nueva Factory para crear el ViewModel
    val factory = PerfilUsuarioViewModelFactory(LocalContext.current.applicationContext as Application)
    val viewModel: PerfilUsuarioViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isLoggedOut) {
        if (uiState.isLoggedOut) {
            onCerrarSesion()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.user != null) {
                // --- ¡CORREGIDO! Usamos los nombres de propiedad correctos del modelo UsuarioLocal ---
                val user = uiState.user!!

                Text("Bienvenido, ${user.userName}", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(24.dp))
                InfoRow("Nombre:", user.name)
                InfoRow("Apellido:", user.lastName)
                InfoRow("Email:", user.email)
                // birthDate es un String, así que lo mostramos directamente
                InfoRow("Fecha de Nacimiento:", user.birthDate)
                // La fecha de registro no está en el modelo local, así que la omitimos.

                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { viewModel.logout() }) {
                    Text("Cerrar Sesión")
                }
            } else {
                Text("No se pudo cargar la información del usuario.")
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(0.4f))
        Text(text = value, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(0.6f))
    }
}
