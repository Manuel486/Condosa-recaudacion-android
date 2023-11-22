package com.example.recaudacion.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


// Cada campo tiene una anotación @SerialName que permite especificar un nombre específico que
// se utilizará al serializar/deserializar el objeto, lo que puede ser útil si los nombres 
// de los campos difieren en la representación serializada.

/* Información personal */

@Serializable
data class InformacionPersonal(
    @SerialName("idCuenta")  val idCuenta: Integer,
    @SerialName("nombre")  val nombre: String,
    @SerialName("banco")  val banco: String,
    @SerialName("moneda")  val moneda: String,
    @SerialName("nroCuenta")  val nroCuenta: Long,
    @SerialName("otro")  val otro: Long
)
