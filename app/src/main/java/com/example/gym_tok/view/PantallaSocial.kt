package com.example.gym_tok.view

import android.media.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.collections.listOf

@Composable
fun PantallaSocial(){}
/*


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

        val demoPosts = remember{
            listOf(
                // Si no quieres usar data class todavía, puedes comentar esta lista
                // y simplemente no dibujar Postcard (o dibujar Placeholders)
                UiPost(id = 1, user= "Camila", time = "12:30", text = "Hola, esto es una prueba" )
        }

        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(demoPosts, key = {it.id}){ post ->
                Postcard(

                )

            }

        }

    }
}

@Composable
fun UiPost(id: Int) {
    TODO("Not yet implemented")
}

@Composable
private fun PostComposer(
    onPickImage: () -> Unit,
    onPublish: () -> Unit
){
    // Solo UI: el texto lo guardamos aqui para renderizar; la "publicacion real" ira luego
    var text by rememberSaveable { mutableStateOf("") }

}

private fun Postcard(
    post: UIPost
){}
*/