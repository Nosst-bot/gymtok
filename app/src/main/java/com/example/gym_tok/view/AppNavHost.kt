package com.example.gym_tok.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gym_tok.ui.pantallas.PantallaComentarios

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){ PantallaLogin(navController) }

        composable("register"){ FormularioRegistro(navController)}

        composable("home"){ PantallaHome(navController) }

        composable("perfil"){PantallaPerfilUsuario(
            onCerrarSesion = {
                navController.navigate("login"){popUpTo(0) }
            },
            onVolver = {navController.popBackStack()}
        )}
        composable("create_post") {
            PantallaCrearPost(navController = navController)
        }
        composable("camera") {
            PantallaCamara(
                onImageCaptured = {uri ->
                    // Cuando se captura una imagen, NO navegamos.
                    // En su lugar, obtenemos la pantalla ANTERIOR en la pila...
                    val previousBackStackEntry = navController.previousBackStackEntry
                    // ...y le guardamos la URI de la imagen en su 'savedStateHandle'.
                    previousBackStackEntry?.savedStateHandle?.set("image_uri", uri)
                    // Finalmente, simplemente volvemos atrás.
                    navController.popBackStack()
                },
                // --- PARÁMETRO AÑADIDO ---
                // Definimos qué hacer si ocurre un error en la cámara.
                onError = { exception ->
                    // Imprimimos el error para poder depurarlo.
                    exception.printStackTrace()
                    // Y simplemente volvemos a la pantalla anterior.
                    navController.popBackStack()
                }
            )

        }

        // --- ¡NUEVA RUTA AÑADIDA! ---
        composable(
            route = "comentarios/{postId}/{userName}", // La ruta incluye un parámetro dinámico.
            arguments = listOf(navArgument("postId") { type = NavType.LongType },
                navArgument("userName") { type = NavType.StringType }) // Definimos que 'postId' es de tipo Long.

        ) { backStackEntry ->
            // Extraemos el postId de los argumentos de la ruta.
            val postId = backStackEntry.arguments?.getLong("postId")
            val userName = backStackEntry.arguments?.getString("userName")

            // Solo si el postId existe, mostramos la pantalla.
            if (postId != null && userName != null) {
                PantallaComentarios(navController = navController, postId = postId , userName = userName)
            }
        }
        // --- ¡NUEVA RUTA DE EDICIÓN! ---
        composable(
            route = "edit_post/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.LongType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getLong("postId")
            if (postId != null) {
                PantallaEditarPost(navController = navController, postId = postId)
            }
        }


    }
}