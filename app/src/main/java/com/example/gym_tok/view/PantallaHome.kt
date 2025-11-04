package com.example.gym_tok.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


const val TAB_SOCIAL = "social"
const val TAB_EXPLORE = "explore"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(
    onCerrarSesion: () -> Unit
){
        var tab by remember { mutableStateOf(TAB_SOCIAL) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gym-tok")},
                actions = {
                    TextButton(onClick = onCerrarSesion) { Text("Cerrar Sesion") }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { tab = TAB_SOCIAL },
                    icon = {Icon(Icons.Default.Home, contentDescription = "Social")},
                    label = { Text("Social") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { tab= TAB_EXPLORE },
                    icon = {Icon(Icons.Default.Home, contentDescription = "Explorar")},
                    label = { Text("Explorar") }
                )
            }

        }

    ) { innerPadding ->

        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
    ){
            when(tab){
                TAB_EXPLORE -> PantallaExplorar()
                else        -> PantallaSocial()
            }
    }

    }


}






