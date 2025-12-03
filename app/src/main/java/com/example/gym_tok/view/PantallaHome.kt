package com.example.gym_tok.view

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.gym_tok.controller.SocialViewModel
import com.example.gym_tok.controller.SocialViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHome(navController: NavController) {
    var selectedRoute by remember { mutableStateOf("social") }

    val context = LocalContext.current
    val factory = SocialViewModelFactory(context.applicationContext as Application)
    val socialViewModel: SocialViewModel = viewModel(factory = factory)

    // --- INICIO DE LA LÓGICA DE AUTO-REFRESCO ---

    // 1. Observamos la entrada actual en la pila de navegación.
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 2. Este efecto se ejecuta cada vez que esta pantalla (PantallaHome) vuelve a estar visible.
    LaunchedEffect(navBackStackEntry) {
        // 3. Verificamos si la "señal" `post_created` fue dejada por la pantalla anterior.
        val postCreated = navBackStackEntry?.savedStateHandle?.get<Boolean>("post_created")
        if (postCreated == true) {
            // 4. Si la señal existe, le decimos al ViewModel que recargue la lista de posts.
            socialViewModel.loadPosts()
            // 5. Limpiamos la señal para que no se recargue de nuevo si solo rotamos la pantalla.
            navBackStackEntry?.savedStateHandle?.remove<Boolean>("post_created")
        }
    }
    // --- FIN DE LA LÓGICA DE AUTO-REFRESCO ---

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 4.dp,
                tonalElevation = 4.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                TopAppBar(
                    title = { Text("Gym Tok") },
                    actions = {
                        IconButton(onClick = { navController.navigate("perfil") }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Perfil de Usuario")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if(selectedRoute == "social")
            FloatingActionButton(
                onClick = { navController.navigate("create_post") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Post")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    selected = selectedRoute == "social",
                    onClick = { selectedRoute = "social" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedRoute == "explore",
                    onClick = { selectedRoute = "explore" },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explorar") },
                    label = { Text("Explorar") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedRoute) {
                "explore" -> PantallaExplorar()
                else -> PantallaSocial(navController = navController, viewModel = socialViewModel)
            }
        }
    }
}