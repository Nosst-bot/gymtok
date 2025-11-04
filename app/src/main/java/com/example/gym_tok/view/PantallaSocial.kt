package com.example.gym_tok.view

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.gym_tok.model.UiPost
import kotlin.collections.listOf
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Image


@Composable
fun PantallaSocial(){

    //Column = apila elementos en vertical.
    // Aqui usaremos dos grandes bloques:
    // 1 el Composer (caja para escribir tomar / foto / publicar)
    // 2 La lista de posts (solo visual por ahora)
    Column(
        modifier = Modifier
            .fillMaxSize()  // La pantalla ocupa todo el alto/ancho disponible
            .padding(16.dp) // margen general para que el contenido respire
    ){

        //  Composer arriba
        /*
            Por ahora solo dibujamos el componente.
            Las funciones onPickImage / onPublish quedan como TODO para agregar logica mas adelante
         */
        PostComposer(
            onPickImage = {/*  */},
            onPublish = {/*  */}
        )

        // Separadores visuales para que la pantalla no se vea apretada
        Spacer(Modifier.height(12.dp))
        Divider() // Linea sutil para separar el "composer" de la lista
        Spacer(Modifier.height(12.dp))

        // 2) Lista de posts (demo con datos ficticios)
        // Para prototipo, puedes usar una lista fija o vacia.
        // Mas adelante, reemplaa esto con datos reales desde ViewModel/Repos.

        val demoPosts = remember {
            listOf(
                // Si no quieres usar data class todavía, puedes comentar esta lista
                // y simplemente no dibujar PostCard (o dibujar Placeholders).
                UiPost(id = 1, user = "Camila", time = "Hace 2 h", text = "Primer día de piernas 💪"),
                UiPost(id = 2, user = "Diego",  time = "Hace 4 h", text = "Full pecho y tríceps."),
                UiPost(id = 3, user = "Vale",   time = "Ayer",     text = "Cardio + abs. ¡Vamos! 🏃‍♀️")
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),                 // la lista ocupa el espacio restante
            contentPadding = PaddingValues(bottom = 80.dp),    // espacio al final para no “aplastar”
            verticalArrangement = Arrangement.spacedBy(16.dp)  // separación entre cada post
        ) {
            items(demoPosts, key = { it.id }) { post ->
                // "Tarjeta" visual de cada post (UI pura)
                PostCard(
                    post = post,
                    onLike    = { /* TODO: like después */ },
                    onComment = { /* TODO: comentar después */ },
                    onShare   = { /* TODO: compartir después */ }
                )
            }
        }
    }
}

@Composable
fun PostComposer(
    onPickImage: () -> Unit,  // callback para abrir la cámara o galería (lógica después)
    onPublish: () -> Unit     // callback para “publicar” (lógica después)
) {
    // Guardamos el texto que escribe el usuario.
    // rememberSaveable = si rota la pantalla o se recrea el proceso, intenta conservar el texto.
    var text by rememberSaveable { mutableStateOf("") }

    // Contenedor vertical de los controles del composer
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp) // separación visual entre campo y botones
    ) {
        // Campo de entrada donde el usuario escribe su post
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },              // actualiza el estado local
            placeholder = { Text("Comparte tu entrenamiento…") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3                                // no se expande infinito; por ahora hasta 3 líneas
        )

        // Fila de acciones: a la izquierda “Tomar foto”, a la derecha “Publicar”
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween // separa los botones a extremos opuestos
        ) {
            // Botón “Tomar foto”
            // De momento solo llama al callback; más tarde pediremos permisos y abriremos cámara/galería.
            OutlinedButton(onClick = onPickImage) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Cámara")
                Spacer(Modifier.width(8.dp))
                Text("Tomar foto")
            }

            // Botón “Publicar”
            // Por ahora solo dispara el callback. Después podrás:
            // - Validar (texto vacío, imagen, etc.)
            // - Limpiar el campo (text = "")
            Button(onClick = onPublish) {
                Text("Publicar")
            }
        }
    }
}

@Composable
fun PostCard(
    post: UiPost,             // Datos mínimos para “pintar” el post (usuario, tiempo, texto, etc.)
    onLike: () -> Unit,       // Callback para “Me gusta” (lógica después)
    onComment: () -> Unit,    // Callback para “Comentar” (lógica después)
    onShare: () -> Unit       // Callback para “Compartir” (lógica después)
) {
    // Surface = “tarjeta” con forma y elevación para separar visualmente cada post
    Surface(
        shape = RoundedCornerShape(12.dp),   // esquinas redondeadas suaves
        tonalElevation = 2.dp                // leve “sombra”/relieve según el tema (Material 3)
    ) {
        // Column = apila las secciones del post en vertical (header, texto, imagen, acciones)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),             // respiración interna de la tarjeta
            verticalArrangement = Arrangement.spacedBy(8.dp) // separación entre secciones
        ) {
            // ===== Encabezado: avatar + nombre/tiempo + botón de opciones =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Lado izquierdo: avatar (placeholder) + nombre + tiempo
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Avatar circular simple (placeholder con inicial del usuario)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.user.first().uppercase(), // inicial del nombre
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // Nombre del usuario y “hace X tiempo”
                    Column {
                        Text(
                            text = post.user,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = post.time,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Lado derecho: botón de más opciones (por ahora sin acciones)
                IconButton(onClick = { /* TODO: menú (editar, reportar, etc.) */ }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Más opciones")
                }
            }

            // ===== Texto del post (opcional) =====
            if (!post.text.isNullOrBlank()) {
                Text(
                    text = post.text!!,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // ===== Imagen del post (placeholder de tamaño fijo) =====
            // Más adelante aquí cargarás tu imagen real (de cámara/galería).
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Imagen del post (placeholder)",
                    modifier = Modifier.size(64.dp)
                )
            }

            // ===== Acciones: Me gusta, Comentar, Compartir =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onLike) {
                    Icon(Icons.Default.Favorite, contentDescription = "Me gusta")
                    Spacer(Modifier.width(6.dp))
                    Text("Me gusta")
                }
                TextButton(onClick = onComment) {
                    Icon(Icons.Default.Message, contentDescription = "Comentar")
                    Spacer(Modifier.width(6.dp))
                    Text("Comentar")
                }
                TextButton(onClick = onShare) {
                    Icon(Icons.Default.Share, contentDescription = "Compartir")
                    Spacer(Modifier.width(6.dp))
                    Text("Compartir")
                }
            }

            // ===== Enlace para abrir comentarios (navegación futura) =====
            TextButton(onClick = { /* TODO: navegar a detalle de comentarios */ }) {
                Text("Ver comentarios (demo)")
            }
        }
    }
}

