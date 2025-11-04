package com.example.gym_tok.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Button
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gym_tok.controller.GymViewModel
import androidx.core.net.toUri

@SuppressLint("UseKtx")
@Composable
fun PantallaExplorar(){
    val viewModel: GymViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        Modifier.fillMaxSize().padding(16.dp)){
        Text(text = "Explorar gimnasios",style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        // Lista de gyms
        if (state.isListLoading && state.list.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) { CircularProgressIndicator(modifier = Modifier.size(28.dp)) }
        } else if (state.listError != null) {
            ErrorBanner(texto = state.listError ?: "Error", onDismiss = { viewModel.cargarGyms() })
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(state.list, key = { it.id }) { g ->
                    Card {
                        Column(Modifier.padding(12.dp)) {
                            Text(g.name, style = MaterialTheme.typography.titleSmall)
                            Spacer(Modifier.height(4.dp))
                            AsyncImage(
                                model = g.photoUrl,
                                contentDescription = "Imagen referencial",
                                modifier = Modifier.fillMaxWidth().height(180.dp).clip(
                                    RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(g.address, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Visitar sitio web", color = Color(0xFF1E88E5), textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickable{
                                    val intent = Intent(Intent.ACTION_VIEW, g.websiteUrl.toUri())
                                    context.startActivity(intent)
                                },
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun ErrorBanner(texto: String, onDismiss: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(texto, modifier = Modifier.weight(1f))
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    }
}
