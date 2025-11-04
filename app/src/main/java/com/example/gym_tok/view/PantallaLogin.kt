package com.example.gym_tok.view

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


/*


 */
@Composable
fun PantallaLogin(navController: NavController, ) {

    var email by rememberSaveable() { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {//Titulo de la pantalla
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineSmall
        )
        // Campo: email
        TextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        // Campo: Password
        TextField(
        value = password,
            onValueChange = {password = it},
            label = {Text("Contraseña")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        // Iniciar Sesion
        Button(
            onClick = {navController.navigate("home")},
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Iniciar Sesión")
        }

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        ){
            Text("Registrarse")
        }



    }
}



