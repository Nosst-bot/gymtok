package com.example.gym_tok.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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

    }
}
