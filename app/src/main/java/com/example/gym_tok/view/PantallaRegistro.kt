package com.example.gym_tok.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRegistro(navController: NavController){

    // El profesor usa solo remember, ¿porque?
    var name by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var fecha_nacimiento by rememberSaveable { mutableStateOf("") }
    // ▼ Estados para el dropdown “Sexo”
    val sexOptions = listOf("Masculino", "Femenino")
    var sexo by rememberSaveable { mutableStateOf<String?>(null) } // valor elegido
    var sexoExpanded by remember { mutableStateOf(false) }         // si el menú está abierto

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    )

    {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){

            //Titulo de la pantalla
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall
            )
            Row (modifier = Modifier
                .fillMaxWidth(), // ocupa todo el ancho de la pantalla
                horizontalArrangement = Arrangement.spacedBy(8.dp) // deja espacio entre los campos
            ){
                // Campo: Nombre
                TextField(
                    value = name,
                    onValueChange = {name = it},
                    label = {Text("Nombre")},
                    singleLine = true,
                    modifier = Modifier.weight(1f) //reparte el ancho de la fila
                )
                Spacer(Modifier.width(8.dp))
                // Campo: Apellido
                TextField(
                    value = lastName,
                    onValueChange = {lastName = it},
                    label = {Text("Apellido")},
                    singleLine = true,
                    modifier = Modifier.weight(1f) //
                )
            }
            // Campo: Correo
            TextField(
                value = email,
                onValueChange = {email = it},
                label = {Text("Email")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = username,
                onValueChange = {username = it},
                label = {Text("Nombre de Usuario")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = fecha_nacimiento,
                onValueChange = {fecha_nacimiento = it},
                label = {Text("Fecha de Nacimiento - dd/mm/aaaa")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Campo: Sexo (lista desplegable)
            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = { sexoExpanded = !sexoExpanded }
            ) {
                // TextField “expuesto” que muestra el valor y abre/cierra el menú
                OutlinedTextField(
                    value = sexo ?: "",
                    onValueChange = {  },// lectura sola: no se escribe manualmente
                    readOnly = true, // el valor se elige del menú
                    label = { Text("Sexo") },
                    placeholder = { Text("Selecciona…") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                    modifier = Modifier
                        .menuAnchor()      // ancla el menú a este TextField
                        .fillMaxWidth()
                )
                // Menú desplegable con las 3 opciones
                ExposedDropdownMenu(
                    expanded = sexoExpanded,
                    onDismissRequest = { sexoExpanded = false }
                ) {
                    sexOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sexo = option         // guarda la selección
                                sexoExpanded = false  // cierra el menú
                            }
                        )
                    }
                }
            }

            TextField(
                value = password,
                onValueChange = { password = it },
                label = {Text("Contraseña")},
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {

                    navController.navigate("login")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Button(
                onClick = {navController.navigate("login")

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salir")
            }

        }
    }
}

