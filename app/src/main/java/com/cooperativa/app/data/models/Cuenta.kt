package com.cooperativa.app.data.models

import com.cooperativa.app.data.models.Transaccion
import com.google.gson.annotations.SerializedName

data class Account(
    val id: String,
    val name: String,
    val number: String,
    val balance: Double,
    val currency: String,
    val type: String, // "AHORRO", "CREDITO", "APORTE"
    val montoOtorgado: Double,
    val fechaProx: String,
    val movements: List<Movement>
)

data class Movement(
    val id: String,
    val amount: Double,
    val description: String,
    val date: String,
    val type: MovementType,
    val location: String? = null, // Nueva propiedad para sucursal
    val currencySymbol: String = "$" // Símbolo de moneda
)
enum class AccountStatus {
    ACTIVE, INACTIVE, PENDING, BLOCKED
}

enum class AccountType {
    SAVINGS, CREDIT, INVESTMENT, LOAN
}

enum class MovementType {
    DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT
}

data class AccountResponse(
    @SerializedName("CODIGO")           val codigo: Int,
    @SerializedName("CUENTA")           val cuenta: String?,
    @SerializedName("DESCRIPCIONPRODUCTO") val descripcionProducto: String,
    @SerializedName("MONEDA")           val moneda: String,
    @SerializedName("MONTOTORGADO")     val montoOtorgado: Double?,
    @SerializedName("FECHA_PROX") val fechaProx: String?,    // nuevo campo
    @SerializedName("SALDO")            val saldo: Double,
    @SerializedName("TIPOPRODUCTO")     val tipoProducto: String,
    @SerializedName("MOVIMIENTOS")      val movimientos: List<MovimientoResponse>
)

data class MovimientoResponse(
    @SerializedName("glosa")       val glosa: String,
    @SerializedName("pago_amort")  val pagoAmort: Double?,
    @SerializedName("importe")     val importe: Double?,
    @SerializedName("valor")       val valor: Double?,
    @SerializedName("saldo")       val saldo: Double,
    @SerializedName("fecha")      val fecha: String    // << nuevo
)
