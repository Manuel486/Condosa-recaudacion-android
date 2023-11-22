package com.example.recaudacion.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recaudacion.R
import com.example.recaudacion.navigation.AppScreens
import com.example.recaudacion.network.Recaudacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuPageScreen(
    collectionsViewModel: MenuPrincipalViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val state = collectionsViewModel.collectionsUiState
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var filtroActual by remember { mutableStateOf("Sin ningún filtro") }

    var selectedRechazado by remember { mutableStateOf(false) }
    var selectedAnulado by remember { mutableStateOf(false) }
    var selectedRegistrado by remember { mutableStateOf(false) }
    var selectedValidado by remember { mutableStateOf(false) }

    // Nuevo estado inmutable para las recaudaciones ordenadas
    val recaudacionesFiltradas = remember { mutableStateOf<List<Recaudacion>>(emptyList()) }

    val estadosSeleccionados = mutableSetOf<String>()

    // Actualiza la lista ordenada al inicio y cuando cambia la lista original
    LaunchedEffect(state.recaudaciones) {
        recaudacionesFiltradas.value = state.recaudaciones
    }

    when {
        state.recaudaciones.isEmpty() -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }

        state.recaudaciones.isNotEmpty() -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "logo",
                            modifier = Modifier
                                .size(width = 150.dp, height = 50.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Recaudaciones",
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(fontSize = 30.sp)
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)
                    ) {
                        Button(
                            onClick = {
                                navController.navigate(route = AppScreens.RegisterCollectionScreen.route)
                            },
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Nueva recaudación")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(15.dp))
                }

                item {

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilterChip(
                                onClick = {
                                    selectedRechazado = !selectedRechazado
                                    if (selectedRechazado) {
                                        estadosSeleccionados.add("Rechazado")
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                    } else {
                                        estadosSeleccionados.remove("Rechazado")
                                        if (estadosSeleccionados.isNotEmpty()) {
                                            recaudacionesFiltradas.value =
                                                state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                        } else {
                                            recaudacionesFiltradas.value = state.recaudaciones
                                        }
                                    }
                                },
                                label = {
                                    Text("Rechazado")
                                },
                                selected = selectedRechazado,
                                leadingIcon = if (selectedRechazado) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )

                            FilterChip(
                                onClick = {
                                    selectedAnulado = !selectedAnulado
                                    if (selectedAnulado) {
                                        estadosSeleccionados.add("Anulado")
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                    } else {
                                        estadosSeleccionados.remove("Anulado")
                                        if (estadosSeleccionados.isNotEmpty()) {
                                            recaudacionesFiltradas.value =
                                                state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                        } else {
                                            recaudacionesFiltradas.value = state.recaudaciones
                                        }
                                    }
                                },
                                label = {
                                    Text("Anulado")
                                },
                                selected = selectedAnulado,
                                leadingIcon = if (selectedAnulado) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )

                            FilterChip(
                                onClick = {
                                    selectedRegistrado = !selectedRegistrado
                                    if (selectedRegistrado) {
                                        estadosSeleccionados.add("Registrado")
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                    } else {
                                        estadosSeleccionados.remove("Registrado")
                                        if (estadosSeleccionados.isNotEmpty()) {
                                            recaudacionesFiltradas.value =
                                                state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                        } else {
                                            recaudacionesFiltradas.value = state.recaudaciones
                                        }
                                    }
                                },
                                label = {
                                    Text("Registrado")
                                },
                                selected = selectedRegistrado,
                                leadingIcon = if (selectedRegistrado) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            FilterChip(
                                onClick = {
                                    selectedValidado = !selectedValidado
                                    if (selectedValidado) {
                                        estadosSeleccionados.add("Validado")
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                    } else {
                                        estadosSeleccionados.remove("Validado")
                                        if (estadosSeleccionados.isNotEmpty()) {
                                            recaudacionesFiltradas.value =
                                                state.recaudaciones.filter { it.estado in estadosSeleccionados }
                                        } else {
                                            recaudacionesFiltradas.value = state.recaudaciones
                                        }
                                    }
                                },
                                label = {
                                    Text("Validado")
                                },
                                selected = selectedValidado,
                                leadingIcon = if (selectedValidado) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Done,
                                            contentDescription = "Done icon",
                                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                                        )
                                    }
                                } else {
                                    null
                                },
                            )
                        }
                    }

                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            Text(text = "Filtro: ", fontWeight = FontWeight.Bold)
                            Text(
                                text = filtroActual,
                                softWrap = true,
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.TopEnd)
                        ) {
                            Button(onClick = { expanded = !expanded }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.filter),
                                    contentDescription = "More",
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Menor a mayor según importe") },
                                    onClick = {
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.sortedBy { it.importe }
                                        filtroActual = "Menor a mayor según importe"
                                        Toast.makeText(
                                            context,
                                            "Ordenado de menor a mayor según importe",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mayor a menor según importe") },
                                    onClick = {
                                        recaudacionesFiltradas.value =
                                            state.recaudaciones.sortedByDescending { it.importe }
                                        filtroActual = "Mayor a menor según importe"
                                        Toast.makeText(
                                            context,
                                            "Ordenado de mayor a menor según importe",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Usa sortedRecaudaciones en lugar de state.recaudaciones
                itemsIndexed(recaudacionesFiltradas.value) { index, item ->
                    CardElevation(index, item)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        else -> {
            ErrorScreen(modifier = modifier.fillMaxSize())
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = "Cargando"
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = "Falló al cargar", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun CardElevation(index: Int, recaudacion : Recaudacion) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAE1E7)),
        modifier = Modifier
            .height(210.dp)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = (index * 2).dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(2f),
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.wrapContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD1D5E1))
                ) {
                    val color = when (recaudacion.estado) {
                        "Validado" -> Color(0xFF4CAF50) // Color para el estado "Validado"
                        "Rechazado" -> Color.Red // Color para el estado "Rechazado"
                        "Anulado" -> Color.Gray // Color para el estado "Anulado"
                        "Registrado" -> Color.Blue // Color para el estado "Registrado"
                        else -> Color.Black // Color predeterminado si el estado no coincide con ninguno
                    }

                    Text(
                        text = "${recaudacion.estado}",
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.titleLarge,
                        color = color,
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${recaudacion.predio}",
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                //Text(text = "NrOperacion : ${recaudacion.operacion}")

                Spacer(modifier = Modifier.height(2.dp))

                Text(text = "Importe : ${recaudacion.importe}$")

                Spacer(modifier = Modifier.height(2.dp))

                /*
                Text(
                    text = "${recaudacion.nombre}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )*/

                Spacer(modifier = Modifier.width(4.dp))

                Text(text = "Fecha : ${recaudacion.fechaOperacion}")

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    ),
                    onClick = { /*TODO*/ }
                ) {
                    Text(
                        text = "Mostrar recaudación",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(width = 150.dp, height = 140.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.condominio1),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }
        }
    }
}

