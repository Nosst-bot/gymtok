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


@OptIn(ExperimentalMaterial3Api::class) // Anotación para usar componentes experimentales de Material 3
@Composable
fun FormularioRegistro(navController: NavController, viewModel: RegistroViewModel = viewModel()){


    val ui = viewModel.state.collectAsState().value

    val scope = rememberCoroutineScope();


    var sexoExpanded by remember { mutableStateOf(false) } 
    val sexOptions = listOf("Masculino", "Femenino") // Opciones para el desplegable.
    var sexo by rememberSaveable { mutableStateOf<String?>(null) } 


    // --- ESTRUCTURA PRINCIPAL DE LA UI ---
    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .padding(16.dp),
        contentAlignment = Alignment.Center // Centra su contenido
    )
    {
        // Column organiza los elementos verticalmente, uno debajo del otro.
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp), // Espacio vertical entre cada elemento.
            horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos horizontalmente.
        ){

            // Titulo de la pantalla
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall // Estilo de texto predefinido del tema.
            )
            
            // Row organiza los campos "Nombre" y "Apellido" horizontalmente.
            Row (modifier = Modifier
                .fillMaxWidth(), // ocupa todo el ancho de la pantalla
                horizontalArrangement = Arrangement.spacedBy(8.dp) // deja espacio entre los campos
            ){
                // Campo: Nombre
                TextField(
                    value = ui.name, // El valor viene del ViewModel.
                    onValueChange = viewModel::onNameChange, // Notifica al ViewModel cuando cambia el texto.
                    label = {Text("Nombre")},
                    singleLine = true, // El campo de texto no tendrá múltiples líneas.
                    modifier = Modifier.weight(1f) // reparte el ancho de la fila equitativamente.
                )
                Spacer(Modifier.width(8.dp)) // Un pequeño espacio en blanco.
                // Campo: Apellido
                TextField(
                    value = ui.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    label = {Text("Apellido")},
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Campo: Correo
            TextField(
                value = ui.email,
                onValueChange = viewModel::onEmailChange,
                label = {Text("Email")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, // Muestra el teclado de tipo email.
                    imeAction = ImeAction.Next // El botón "Enter" del teclado será "Siguiente".
                ),
                modifier = Modifier.fillMaxWidth() // Ocupa todo el ancho.
            )
            
            // Campo: Nombre de Usuario
            TextField(
                value = ui.userName,
                onValueChange = viewModel::onUserNameChange,
                label = {Text("Nombre de Usuario")},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    // Aquí el tipo de teclado podría ser `KeyboardType.Text`. `Email` no es ideal.
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Componente personalizado para seleccionar la fecha.
            DatePickerFieldToModal(viewModel=viewModel)

            // --- CAMPO SEXO (MENÚ DESPLEGABLE) ---
            // Este es un componente de Material 3 que combina un campo de texto con un menú.
            ExposedDropdownMenuBox(
                expanded = sexoExpanded, // El estado que controla si está abierto o cerrado.
                onExpandedChange = { sexoExpanded = !sexoExpanded } // Cambia el estado al hacer clic.
            ) {
                // TextField “expuesto” que muestra el valor y abre/cierra el menú
                OutlinedTextField(
                    value = sexo ?: "", // Muestra el sexo elegido, o un texto vacío si es nulo.
                    onValueChange = {}, // No se puede escribir, solo leer.
                    readOnly = true, 
                    label = { Text("Sexo") },
                    placeholder = { Text("Selecciona…") },
                    // Muestra el icono de flecha hacia abajo o arriba.
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexoExpanded) },
                    modifier = Modifier
                        // ancla el menú a este TextField, es importante para que el menú
                        // aparezca en la posición correcta.
                        .menuAnchor()
                        .fillMaxWidth()
                )
                // Menú desplegable con las opciones
                ExposedDropdownMenu(
                    expanded = sexoExpanded,
                    onDismissRequest = { sexoExpanded = false } // Se cierra si se toca fuera del menú.
                ) {
                    sexOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sexo = option         // guarda la selección en el estado local.
                                sexoExpanded = false  // cierra el menú.
                                
                                // Convierte el texto "Masculino" a un carácter 'M' para el ViewModel.
                                val sexChar = when (option) {
                                    "Masculino" -> 'M'
                                    "Femenino" -> 'F'
                                    else -> 'N' // 'N' como valor por defecto o nulo.
                                }
                                viewModel.onSexChange(sexChar) // Notifica al ViewModel.
                            }
                        )
                    }
                }
            }

            // Campo: Contraseña
            TextField(
                value = ui.password,
                onValueChange = viewModel::onPasswordChange,
                label = {Text("Contraseña")},
                singleLine = true,
                // Oculta los caracteres de la contraseña con puntos (•).
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, // Teclado optimizado para contraseñas.
                    imeAction = ImeAction.Done // El botón "Enter" del teclado será "Hecho".
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            // Botón de Registro
            Button(
                onClick = { scope.launch { viewModel.register(navController)}},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
            

            Button(
                onClick = {navController.navigate("login")},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }

            // --- MOSTRAR MENSAJES DE ERROR O ÉXITO ---
            // Este bloque se mostrará solo si `errorMessage` en el ViewModel no es nulo.
            ui.errorMessage?.let {
                InputChip(
                    onClick = { /* No hace nada al hacer clic */ },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    label = { Text(it, fontSize = 18.sp) }, // Muestra el mensaje de error.
                    selected = false,
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = Color.Red, // Fondo rojo para errores.
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }

            // Este bloque se mostrará solo si `successMessage` no es nulo.
            ui.successMessage?.let {
                InputChip(
                    onClick = { /* No hace nada al hacer clic */ },
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    label = { Text(it, fontSize = 18.sp) }, // Muestra el mensaje de éxito.
                    selected = false,
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = Color.Green, // Fondo verde para éxito.
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }
        }
    }
}

// --- COMPONENTE PERSONALIZADO PARA EL CAMPO DE FECHA ---
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier, viewModel: RegistroViewModel) {
    val birthDate = viewModel.state.value.birthDate // Obtiene la fecha actual del ViewModel.
    // Estado local para controlar la visibilidad del diálogo del selector de fecha.
    var showModal by remember { mutableStateOf(false) }

    // Un campo de texto que parece un campo normal, pero no es editable.
    OutlinedTextField(
        value = birthDate,
        onValueChange = {}, // No editable manualmente.
        label = { Text("Fecha de Nacimiento") },
        placeholder = { Text("DD-MM-YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Seleccionar fecha")
        },
        modifier = modifier
            .fillMaxWidth()
            // Este modificador es clave. Detecta un clic completo (presionar y soltar)
            // en el campo de texto para mostrar el diálogo.
            .pointerInput(Unit) {
                awaitEachGesture {
                    // Espera a que el usuario presione el dedo.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    // Espera a que el usuario levante el dedo.
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    // Si el gesto no fue cancelado, muestra el modal.
                    if (upEvent != null) showModal = true
                }
            }
    )

    // Si `showModal` es true, se muestra el diálogo del selector de fecha.
    if (showModal) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let { // Si el usuario seleccionó una fecha (no es nulo).
                    // Convierte los milisegundos a un formato de fecha legible (DD-MM-YYYY).
                    val formattedDate = convertMillisToDate(it)
                    // Notifica al ViewModel sobre el cambio de fecha.
                    viewModel.onBirthDateChange(formattedDate)
                }
                showModal = false // Cierra el diálogo.
            },
            onDismiss = { showModal = false } // Cierra el diálogo si se toca fuera o se cancela.
        )
    }
}

// --- DIÁLOGO DEL SELECTOR DE FECHA (DATE PICKER) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    // Estado interno del DatePicker para saber qué fecha está seleccionada.
    val datePickerState = rememberDatePickerState()

    // Diálogo que muestra el calendario.
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Al pulsar OK, se pasa la fecha seleccionada (en milisegundos)
                // al callback `onDateSelected`.
                onDateSelected(datePickerState.selectedDateMillis)
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        // El componente de calendario de Material 3.
        DatePicker(state = datePickerState)
    }
}

// --- FUNCIÓN DE UTILIDAD ---
// Convierte un valor de tiempo en milisegundos (Long) a un String con formato "dd-MM-yyyy".
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC") // Importante para evitar problemas de zona horaria.
    return formatter.format(Date(millis))
}
