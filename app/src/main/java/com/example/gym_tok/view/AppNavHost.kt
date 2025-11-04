package com.example.gym_tok.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){ PantallaLogin(navController) }
        composable("home"){ PantallaHome()}
        //Register
        composable("register"){ FormularioRegistro(navController) }

}
}
