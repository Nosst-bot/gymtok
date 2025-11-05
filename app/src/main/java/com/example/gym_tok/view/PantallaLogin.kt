package com.example.gym_tok.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gym_tok.controller.LoginViewModel
import kotlinx.coroutines.launch


/*


 */
@Composable
fun PantallaLogin(navController: NavController, viewModel: LoginViewModel = viewModel()) {

    val ui = viewModel.state.collectAsState().value;
    val scope = rememberCoroutineScope();

    Box(
        modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center

    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {//Titulo de la pantalla
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineSmall
            )
            // Campo: email
            TextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                label = {Text("Email")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            // Campo: Password
            TextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                label = {Text("Contraseña")},
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            // Iniciar Sesion
            Button(
                enabled = !ui.isLoading,
                onClick = { scope.launch { viewModel.login(navController) }},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if(ui.isLoading) "Ingresando..." else "Iniciar Sesión")
            }
            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Registrarse")
            }

            ui.errorMessage?.let { Text(it, color = Color.Red) }
        }
    }
}



