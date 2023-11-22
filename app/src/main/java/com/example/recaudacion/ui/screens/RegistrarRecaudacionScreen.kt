package com.example.recaudacion.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recaudacion.R
import com.example.recaudacion.navigation.AppScreens
import com.example.recaudacion.network.CuentaDTO
import com.example.recaudacion.network.CuentaPredioDTO
import com.example.recaudacion.network.MantenimientoReciboDTO
import com.example.recaudacion.network.RecaudacionDTO
import com.example.recaudacion.network.RecaudacionEstadoDTO
import com.example.recaudacion.network.TipoMonedaDTO
import java.util.Calendar


@Composable
fun RegisterCollection1PageScreen(
    registroViewModel: RegistroViewModel,
    navController: NavController
) {
    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(navController = navController)
        Spacer(modifier = Modifier.height(20.dp))
        FormReceipt(registroViewModel, navController, currentIndex) {}
    }
}


@Composable
fun Header(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }

    // Función para mostrar la alerta
    fun showAlertDialog() {
        showDialog = true
    }

    // Función para manejar la acción del botón en la alerta
    fun handleConfirm() {
        // Aquí puedes ejecutar la acción de confirmación
        // Por ejemplo, cerrar la pantalla, eliminar algo, etc.
        navController.navigate(route = AppScreens.MainMenuScreen.route)
        showDialog = false
    }

    // Contenido de la alerta
    val alertDialog = if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Manejar la acción de cerrar la alerta (opcional)
                showDialog = false
            },
            title = { Text(text = "Confirmación") },
            text = { Text(text = "¿Estás seguro de regresar a la interfaz principal?") },
            confirmButton = {
                Button(
                    onClick = {
                        handleConfirm()
                        showDialog = false
                    }
                ) {
                    Text(text = "Sí")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(text = "Cancelar")
                }
            }
        )
    } else null


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween  // Cambiado a SpaceBetween
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(width = 150.dp, height = 50.dp)
        )
        IconButton(
            onClick = { showAlertDialog() }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Close",
                tint = Color.Black,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}


@Composable
fun mostrarAlerta(titulo: String, errorMessage: String, registroViewModel: RegistroViewModel) {
    AlertDialog(
        onDismissRequest = { /* No hacemos nada al cerrar */ },
        title = { Text(text = titulo) },
        text = { Text(text = errorMessage) },
        confirmButton = {
            TextButton(onClick = {
                registroViewModel.clearErrorMessage()
            }) {
                Text("Aceptar")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormReceipt(
    registroViewModel: RegistroViewModel,
    navController: NavController,
    currentIndex: Int,
    onNext: () -> Unit
) {
    val context = LocalContext.current.applicationContext

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Registrar recaudación",
        fontWeight = FontWeight.Bold,
        style = TextStyle(fontSize = 30.sp),
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {

        val nroRecibo = remember { mutableStateOf(TextFieldValue()) }
        val estadoRecibo = registroViewModel.registerUiState.estadoRecibo
        val importeRecibo = registroViewModel.registerUiState.importeRecibo

        val nrOperacion = remember { mutableStateOf(TextFieldValue()) }
        var fechaOperacion: String by rememberSaveable {
            mutableStateOf("")
        }
        val anio: Int
        val mes: Int
        val dia: Int
        val mCalendar = Calendar.getInstance()
        anio = mCalendar.get(Calendar.YEAR)
        mes = mCalendar.get(Calendar.MONTH)
        dia = mCalendar.get(Calendar.DAY_OF_MONTH)
        val importeRecaudacion = remember { mutableStateOf(TextFieldValue()) }
        val observacion = remember { mutableStateOf(TextFieldValue()) }

        TextField(
            label = { Text(text = "N° recibo") },
            value = nroRecibo.value,
            singleLine = true,
            placeholder = { Text(text = "REC0001",color=Color.LightGray ) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { nroRecibo.value = it },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

        )
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
                registroViewModel.getMantenimientoReciboPorNroRecibo(nroRecibo.value)
            },
            modifier = Modifier.width(200.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp) // Tamaño del icono
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Buscar")
            }
        }
        Spacer(modifier = Modifier.height(35.dp))

        if (registroViewModel.registerUiState.idMantRecibo != 0) {
            TextField(
                label = { Text(text = "Estado del recibo") },
                value = estadoRecibo,
                singleLine = true,
                enabled = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = { },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(25.dp))


            TextField(
                label = { Text(text = "Monto del recibo") },
                value = importeRecibo.toString(),
                singleLine = true,
                enabled = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(25.dp))

            TextField(
                label = { Text(text = "Número de operación") },
                value = nrOperacion.value,
                singleLine = true,
                enabled = true,
                placeholder = { Text(text = "202002118",color=Color.LightGray ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { nrOperacion.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(25.dp))

            // Fecha de operacion

            val mDataPickerDialog = DatePickerDialog(
                LocalContext.current,
                { Datepicker, anio: Int, mes: Int, dia: Int ->
                    fechaOperacion = "$anio-${mes + 1}- $dia"
                }, anio, mes, dia
            )


            Box(modifier = Modifier.fillMaxWidth()) {
                Row {
                    TextField(
                        value = fechaOperacion,
                        onValueChange = { fechaOperacion = it },
                        readOnly = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                        label = {
                            Text(
                                text = "Fecha de operación"
                            )
                        })
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp)
                            .padding(4.dp)
                            .clickable { mDataPickerDialog.show() })

                }
            }
            Spacer(modifier = Modifier.height(25.dp))

            TextField(
                label = { Text(text = "Importe a pagar") },
                value = importeRecaudacion.value,
                singleLine = true,
                enabled = true,
                placeholder = { Text(text = "0.0",color=Color.LightGray ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { importeRecaudacion.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(25.dp))

            TextField(
                label = { Text(text = "Observación") },
                value = observacion.value,
                enabled = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                onValueChange = { observacion.value = it },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 90.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))

            )
            Spacer(modifier = Modifier.height(25.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        if (registroViewModel.registerUiState.idMantRecibo != 0 && nrOperacion.value.text != "" && fechaOperacion != null
                            && importeRecaudacion.value.text != "" && observacion.value.text != ""
                        ) {

                            val cuentaDTO =
                                CuentaDTO(idCuenta = registroViewModel.registerUiState.idCuenta)
                            val mantenimientoReciboDTO =
                                MantenimientoReciboDTO(idMantenimientoRecibo = registroViewModel.registerUiState.idMantRecibo)
                            val tipoMonedaDTO =
                                TipoMonedaDTO(idTipoMoneda = registroViewModel.registerUiState.idTipoMoneda)
                            val recaudacionEstadoDTO = RecaudacionEstadoDTO(idRecaudacionEstado = 4) // Registrado
                            val cuentaPredioDTO =
                                CuentaPredioDTO(idCuentaPredio = registroViewModel.registerUiState.idCuentaPredio)

                            val recaudacionDTO = RecaudacionDTO(
                                cuenta = cuentaDTO,
                                mantenimientoRecibo = mantenimientoReciboDTO,
                                noperacion = nrOperacion.value.text.toLong(),
                                fechaOperacion = fechaOperacion,
                                tipoMoneda = tipoMonedaDTO,
                                importe = importeRecaudacion.value.text.toDouble(),
                                recaudacionEstado = recaudacionEstadoDTO,
                                cuentaPredio = cuentaPredioDTO,
                                observacion = observacion.value.text
                            )

                            registroViewModel.guardarRecaudacion(recaudacionDTO)
                            Toast.makeText(context, "Recaudación registrada!", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(route = AppScreens.MainMenuScreen.route)
                        } else {
                            Toast.makeText(
                                context,
                                "Debe completar todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF000080)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .width(100.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF000080),
                            shape = RoundedCornerShape(15.dp)
                        )
                ) {
                    Text(text = "Guardar")
                }
            }
        }

        // Observa si hay un mensaje de error y muestra una alerta en consecuencia
        val errorMessage = registroViewModel.errorMessage
        if (errorMessage.value.isNotEmpty()) {
            mostrarAlerta("Error", errorMessage = errorMessage.value, registroViewModel)
        }


    }
}


fun DatePicker() {
    val anio: Int
}