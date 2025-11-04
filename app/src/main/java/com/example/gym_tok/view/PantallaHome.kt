package com.example.gym_tok.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun PantallaHome () {
    val nav = rememberNavController()
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gym Tok") },)
        },

        bottomBar = {
            NavigationBar {

            }
        }


    )
}
