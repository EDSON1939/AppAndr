package com.cooperativa.app.data.models

data class Transaccion(
    val tipo: TipoTransaccion,
    val monto: Double,
    val sucursal: String,
    val fecha: String
)

enum class TipoTransaccion {
    RETIRO,
    DEPOSITO
}
