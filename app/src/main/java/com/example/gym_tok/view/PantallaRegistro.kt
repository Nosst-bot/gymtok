package com.example.gym_tok.view

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_tok.controller.FormularioRegistroViewModel
import com.example.gym_tok.controller.FormularioRegistroViewModelFactory
import com.example.gym_tok.model.User
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRegistro(navController: NavController){

    val factory = FormularioRegistroViewModelFactory(LocalContext.current.applicationContext as Application)
    val viewModel: FormularioRegistroViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isRegistrationSuccessful) {
        if (uiState.isRegistrationSuccessful) {
            navController.navigate("login")
        }
    }

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf<LocalDate?>(null) }
    var sexo by remember { mutableStateOf<Char?>(null) }
    var sexoExpanded by remember { mutableStateOf(false) }
    val sexOptions = listOf("Masculino", "Femenino")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall
            )

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                TextField(value = nombre, onValueChange = { nombre = it }, label = {Text("Nombre")}, singleLine = true, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                TextField(value = apellido, onValueChange = { apellido = it }, label = {Text("Apellido")}, singleLine = true, modifier = Modifier.weight(1f))
            }

            TextField(value = email, onValueChange = { email = it }, label = {Text("Email")}, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())
            TextField(value = nombreUsuario, onValueChange = { nombreUsuario = it }, label = {Text("Nombre de Usuario")}, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next), modifier = Modifier.fillMaxWidth())

            DatePickerFieldToModal(
                selectedDate = fechaNacimiento,
                onDateSelected = { fechaNacimiento = it }
            )

            ExposedDropdownMenuBox(expanded = sexoExpanded, onExpandedChange = { sexoExpanded = !sexoExpanded }) {
                OutlinedTextField(
                    value = when(sexo) { 'M' -> "Masculino"; 'F' -> "Femenino"; else -> "" },
                    onValueChange = {}, readOnly = true, label = { Text("Sexo") },
                    placeholder = { Text("Selecciona…") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = sexoExpanded, onDismissRequest = { sexoExpanded = false }) {
                    sexOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sexo = when (option) { "Masculino" -> 'M'; "Femenino" -> 'F'; else -> null }
                                sexoExpanded = false
                            }
                        )
                    }
                }
            }

            TextField(value = contrasena, onValueChange = { contrasena = it }, label = {Text("Contraseña")}, singleLine = true, visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done), modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {

                    if (fechaNacimiento != null && sexo != null) {
                        val user = User(
                            name = nombre,
                            lastName = apellido,
                            userName = nombreUsuario,
                            email = email,
                            sex = sexo!!,
                            birthDate = fechaNacimiento.toString(),
                            password = contrasena
                        )
                        viewModel.registerUser(user)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Button(onClick = {navController.navigate("login")}, modifier = Modifier.fillMaxWidth()) { Text("Volver") }

            uiState.errorMessage?.let {
                InputChip(
                    onClick = { }, modifier = Modifier.fillMaxWidth().height(55.dp),
                    label = { Text(it, fontSize = 18.sp) }, selected = false,
                    colors = InputChipDefaults.inputChipColors(containerColor = Color.Red, labelColor = MaterialTheme.colorScheme.onPrimary)
                )
            }
        }
    }
}

@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    var showModal by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    OutlinedTextField(
        value = selectedDate?.format(formatter) ?: "",
        onValueChange = {}, readOnly = true, label = { Text("Fecha de Nacimiento") },
        placeholder = { Text("DD-MM-YYYY") },
        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha") },
        modifier = modifier.fillMaxWidth().pointerInput(Unit) {
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
                    val date = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                    onDateSelected(date)
                }
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) { Text("OK") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    ) {
        DatePicker(state = datePickerState)
    }
}