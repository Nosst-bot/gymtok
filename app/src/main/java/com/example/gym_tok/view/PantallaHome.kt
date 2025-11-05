package com.example.gym_tok.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navController: NavController){
        var tab by remember { mutableStateOf("social") }
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp, //Genera sombra
                tonalElevation = 4.dp, // da mas sensacion de profundidad
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) // linea fina
            ) {
                TopAppBar(
                title = { Text("Gym-tok")},
                actions = {
                    IconButton(onClick = {navController.navigate("perfil")}) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil de Usuario")
                    }
                    }
                )
            }
        },

        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface  // color base
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = { tab = "social" },
                        icon = {Icon(Icons.Default.Home, contentDescription = "Inicio")},
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { tab= "explore" },
                        icon = {Icon(Icons.Default.Search, contentDescription = "Explorar")},
                        label = { Text("Buscar Gimnasios") }
                    )
                }
            }
        }
    ) { innerPadding ->

        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
    ){
            when(tab){
                "explore" -> PantallaExplorar()
                else        -> PantallaSocial()
            }
        }
    }
}
