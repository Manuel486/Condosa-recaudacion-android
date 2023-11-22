package com.example.recaudacion.network

//Esta clase es un modelo de datos que contiene información 
// relacionada con registros de cuentas, predios y mantenimiento de recibos.

data class RegistroState(
    // Informacion Personal
    val idCuenta: Int = 0,
    val nombre: String = "",
    val banco: String = "",
    val idTipoMoneda: Int = 0,
    val moneda: String = "",
    val nroCuenta: Long = 0L,

    // Información del predio
    val idCuentaPredio: Int = 0,
    val nombrePredio: String = "",
    val direccion: String = "",
    val tipoPredio: String = "",
    val nroCuentaPredio: Long = 0L,
    val tipoDeAutorizacion: String = "",
    val estado: String = "",

    // Mantenimiento del recibo
    val idMantRecibo: Int = 0,
    val estadoRecibo: String = "",
    val importeRecibo: Double = 0.0,

    // Recaudacion
    val nOperacion: Long = 0L,
    val fechaOperacion: String = "",
    val recaudacionImporte: Double = 0.0,
    val idRecaudacionEstado: Int = 0,
    val observacion: String = ""
)
