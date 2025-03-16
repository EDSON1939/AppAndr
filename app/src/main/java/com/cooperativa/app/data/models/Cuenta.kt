package com.cooperativa.app.data.models

import com.cooperativa.app.data.models.Transaccion


data class Cuenta(
    val id: String,
    val tipoCuenta: TipoCuenta,
    val nombreCuenta: String,
    val numeroCuenta: String,
    val saldo: Double,
    val transacciones: List<Transaccion>
)

enum class TipoCuenta {
    AHORRO, CREDITO, APORTE
}
