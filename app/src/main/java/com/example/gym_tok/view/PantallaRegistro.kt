package com.example.gym_tok.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_tok.controller.RegistroViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRegistro(navController: NavController, viewModel: RegistroViewModel = viewModel()){

    val ui = viewModel.state.collectAsState().value
    val scope = rememberCoroutineScope();
    var showSuccess by remember { mutableStateOf(false) }

    var sexoExpanded by remember { mutableStateOf(false) }
    val sexOptions = listOf("Masculino", "Femenino")
    var sexo by rememberSaveable { mutableStateOf<String?>(null) } // valor elegido


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
                    value = ui.name,
                    onValueChange = viewModel::onNameChange,
                    label = {Text("Nombre")},
                    singleLine = true,
                    modifier = Modifier.weight(1f) //reparte el ancho de la fila
                )
                Spacer(Modifier.width(8.dp))
                // Campo: Apellido
                TextField(
                    value = ui.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    label = {Text("Apellido")},
                    singleLine = true,
                    modifier = Modifier.weight(1f) //
                )
            }
            // Campo: Correo
            TextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                label = {Text("Email")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = ui.userName,
                onValueChange = viewModel::onUserNameChange,
                label = {Text("Nombre de Usuario")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            DatePickerFieldToModal(viewModel=viewModel)

            // Campo: Sexo (lista desplegable)
            ExposedDropdownMenuBox(
                expanded = sexoExpanded,
                onExpandedChange = { sexoExpanded = !sexoExpanded }
            ) {
                // TextField “expuesto” que muestra el valor y abre/cierra el menú
                OutlinedTextField(
                    value = sexo ?: "",
                    onValueChange = {},// lectura sola: no se escribe manualmente
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
                                sexoExpanded = false
                            val sexChar = when (option) {
                                "Masculino" -> 'M'
                                "Femenino" -> 'F'
                                else -> 'N'
                            }// cierra el menú
                                viewModel.onSexChange(sexChar)
                            }
                        )
                    }
                }
            }

            TextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
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
                onClick = { scope.launch { viewModel.register(navController)}},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Button(
                onClick = {navController.navigate("login")

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }

            ui.errorMessage?.let {
                InputChip(
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    label = { Text(it, fontSize = 18.sp) },
                    selected = false,
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = Color.Red,
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                        trailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }

            ui.successMessage?.let {
                InputChip(
                    onClick = {

                    },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    label = { Text(it, fontSize = 18.sp) },
                    selected = false,
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = Color.Green,
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                        trailingIconColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }

        }
    }
}
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier, viewModel: RegistroViewModel) {
    val birthDate = viewModel.state.value.birthDate // el valor actual en el estado
    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = birthDate,
        onValueChange = {}, // no editable manualmente
        label = { Text("Fecha de Nacimiento") },
        placeholder = { Text("DD-MM-YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) showModal = true
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val formattedDate = convertMillisToDate(it)
                    viewModel.onBirthDateChange(formattedDate) // ⬅️ ahora sí notificamos al VM
                }
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}
