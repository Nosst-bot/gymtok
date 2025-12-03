package com.example.gym_tok.view

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
// --- 1. SE AÑADE EL ICONO DE FLECHA HACIA ATRÁS ---
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gym_tok.controller.CrearPostViewModel
import com.example.gym_tok.controller.CrearPostViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearPost(
    navController: NavController
) {
    val context = LocalContext.current
    val factory = CrearPostViewModelFactory(context.applicationContext as android.app.Application)
    val viewModel: CrearPostViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    val savedImageUri = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<Uri?>("image_uri", null)
        ?.collectAsState()

    LaunchedEffect(savedImageUri?.value) {
        savedImageUri?.value?.let { uri ->
            viewModel.onImageUriReceived(uri)
            navController.currentBackStackEntry?.savedStateHandle?.remove<Uri>("image_uri")
        }
    }

    // --- ¡AQUÍ ESTÁ LA MAGIA! ---
    LaunchedEffect(uiState.postCreatedSuccessfully) {
        if (uiState.postCreatedSuccessfully) {
            // 1. Antes de volver, dejamos una "señal" en la pantalla anterior.
            navController.previousBackStackEntry?.savedStateHandle?.set("post_created", true)

            // 2. Reseteamos el estado en el ViewModel para evitar bucles.
            viewModel.onPostPublished()

            // 3. Volvemos a la pantalla anterior.
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nuevo Post") },
                // --- 2. SE AÑADE EL BOTÓN DE NAVEGACIÓN (VOLVER) ---
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // El resto de la UI no cambia
            OutlinedTextField(
                value = uiState.postText,
                onValueChange = { viewModel.onPostTextChanged(it) },
                label = { Text("Escribe tu entrenamiento...") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
            )

            if (uiState.imageUri != null) {
                AsyncImage(
                    model = uiState.imageUri,
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Button(
                    onClick = { navController.navigate("camera") },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Añadir foto")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir Foto")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.publishPost() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = (uiState.postText.isNotBlank() || uiState.imageUri != null) && !uiState.isPublishing
            ) {
                if (uiState.isPublishing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Publicar")
                }
            }
        }
    }
}