package com.example.recaudacion.network


/* Analizar los estados de la recaudacion
    Anulado (No existe deberia ser otro CUS
    Rechazado
    Registrado - Estado una vez que realizamos nuestro CUS
    Validado
 */
data class RecaudacionState(
    val recaudaciones: List<Recaudacion> = emptyList()
)