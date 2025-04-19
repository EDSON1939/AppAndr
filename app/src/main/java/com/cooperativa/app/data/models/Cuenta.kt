package com.cooperativa.app.data.models

import com.cooperativa.app.data.models.Transaccion

data class Account(
    val id: String,
    val name: String,
    val number: String,
    val balance: Double,
    val currency: String,
    val type: String, // "AHORRO", "CREDITO", "APORTE"
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