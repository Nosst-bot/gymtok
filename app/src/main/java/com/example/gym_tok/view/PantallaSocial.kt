package com.example.gym_tok.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gym_tok.controller.SocialViewModel
import com.example.gym_tok.model.UiPost

@Composable
fun PantallaSocial(
    navController: NavController,
    viewModel: SocialViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val shouldRefresh = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("post_created", false)
        ?.collectAsState()

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.loadPosts()
            navController.currentBackStackEntry?.savedStateHandle?.set("post_created", false)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "Error al cargar los posts.",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.posts, key = { it.id }) { post ->
                    PostCard(
                        post = post,
                        onLike = { viewModel.onLikeClicked(post.id) },
                        onComment = { navController.navigate("comentarios/${post.id}/${post.user}") },
                        onDelete = { viewModel.deletePost(post.id) },
                        onEdit = { navController.navigate("edit_post/${post.id}") },
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: UiPost,
    onLike: () -> Unit,
    onComment: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp
    ) {
        // Esta es la Column principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- CABECERA DEL POST ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = post.user.firstOrNull()?.uppercase() ?: "G")
                    }
                    Column {
                        Text(text = post.user, style = MaterialTheme.typography.titleMedium)
                        Text(text = post.time, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                onEdit()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Borrar") },
                            onClick = {
                                onDelete()
                                showMenu = false
                            }
                        )
                    }
                }
            }



            val hasText = !post.text.isNullOrBlank()

            // Se añade un espaciador solo si hay texto o imagen para mostrar
            if (hasText || post.imageUrl != null) {
                Spacer(modifier = Modifier.height(16.dp))
            }


            if (hasText) {
                Text(text = post.text!!, style = MaterialTheme.typography.bodyLarge)
            }

            // Mostramos la imagen si existe
            if (post.imageUrl != null) {
                Spacer(modifier = Modifier.height(if (hasText) 12.dp else 0.dp))
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = "Imagen del post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- ACCIONES (LIKE, COMENTARIO) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val likeIcon = if (post.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
                val likeColor = if (post.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onLike) {
                        Icon(likeIcon, contentDescription = "Me gusta", tint = likeColor)
                    }
                    Text(text = "${post.likesCount}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onComment) {
                        Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comentar", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

        }
    }
}
