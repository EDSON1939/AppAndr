package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.AccountType
import com.cooperativa.app.data.models.Movement
import com.cooperativa.app.data.models.MovementType
import kotlinx.coroutines.delay
import javax.inject.Inject
import retrofit2.Response

class AccountsServiceImpl @Inject constructor(
    private val api: AccountsService,
    private val tokenManager: TokenManager
) {
    suspend fun getAccounts(): List<Account> {
       /* val token = tokenManager.getToken() ?: throw Exception("No autenticado")
        val response = api.getAccounts("Bearer $token")

        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string() ?: "Error al obtener cuentas")
        }*/


        delay(800) // Simular latencia de red

        val mockData = listOf(
            Account(
                id = "112000730",
                name = "CREDITO AUTOMOVIL",
                number = "NR 112000730",
                type = AccountType.LOAN,
                balance = 0.01,
                currency = "Bs.",
                movements = listOf(
                    Movement(
                        id = "mov1",
                        amount = 32.00,
                        description = "DEPOSITO",
                        date = "18 Sept 2019",
                        type = MovementType.DEPOSIT,
                        location = "SUCURSAL EL ALTO",
                        currencySymbol = "$"
                    ),
                    Movement(
                        id = "mov2",
                        amount = 32.00,
                        description = "DEPOSITO",
                        date = "12 Sept 2019",
                        type = MovementType.DEPOSIT,
                        location = "SUCURSAL IRPAN",
                        currencySymbol = "$"
                    ),
                    Movement(
                        id = "mov3",
                        amount = 32.00,
                        description = "DEPOSITO",
                        date = "10 Sept 2019",
                        type = MovementType.DEPOSIT,
                        location = "SUCURSAL EL ALTO",
                        currencySymbol = "$"
                    ),
                    Movement(
                        id = "mov4",
                        amount = 421.00,
                        description = "DEPOSITO",
                        date = "06 Sept 2019",
                        type = MovementType.DEPOSIT,
                        location = "SUCURSAL IRPAN",
                        currencySymbol = "$"
                    )
                )
            ),
            Account(
                id = "112000731",
                name = "CUENTA DE AHORROS",
                number = "NR 112000731",
                type = AccountType.SAVINGS,
                balance = 1250.75,
                currency = "Bs.",
                movements = listOf(
                    Movement(
                        id = "mov5",
                        amount = 500.00,
                        description = "DEPOSITO INICIAL",
                        date = "01 Oct 2019",
                        type = MovementType.DEPOSIT,
                        currencySymbol = "Bs."
                    )
                )
            )
        )

        return (mockData)


    }
}