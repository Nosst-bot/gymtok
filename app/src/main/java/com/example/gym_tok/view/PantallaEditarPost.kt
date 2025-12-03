package com.example.gym_tok.view

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gym_tok.controller.EditPostViewModel
import com.example.gym_tok.controller.EditPostViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPost(
    navController: NavController,
    postId: Long
) {
    val context = LocalContext.current
    val factory = EditPostViewModelFactory(context.applicationContext as Application, postId)
    val viewModel: EditPostViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    // Efecto para navegar hacia atrás cuando el ViewModel lo indique
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            // 1. Dejamos una "nota" en la pantalla anterior para que se refresque.
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("post_edited", true)

            // 2. Volvemos atrás
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Post") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de texto para la descripción
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Vista previa de la imagen y botón de borrar
                if (uiState.imageUrl != null) {
                    Box {
                        AsyncImage(
                            model = "http://10.0.2.2:8080" + uiState.imageUrl,
                            contentDescription = "Imagen del post",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        // Botón para borrar la imagen
                        IconButton(
                            onClick = { viewModel.onDeleteImage() },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar Imagen", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón de guardar
                Button(
                    onClick = { viewModel.saveChanges() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }

            }
        }
    }
}