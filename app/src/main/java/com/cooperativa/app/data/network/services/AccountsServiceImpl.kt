package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.AccountDetailDto
import com.cooperativa.app.data.models.AccountType
import com.cooperativa.app.data.models.Movement
import com.cooperativa.app.data.models.MovementDto
import com.cooperativa.app.data.models.MovementType
import com.cooperativa.app.data.models.PagedMovementsResponse
import kotlinx.coroutines.delay
import retrofit2.HttpException
import javax.inject.Inject
import retrofit2.Response
import java.util.UUID


class AccountsServiceImpl @Inject constructor(
    private val api: AccountsService,
    private val tokenManager: TokenManager
) {
    suspend fun getAccounts(tipo: Int): List<Account> {
        val token = tokenManager.getToken() ?: throw Exception("No autenticado")
        val response = api.getAccounts("Bearer $token", tipo)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string() ?: "Error al obtener cuentas")
        }
        val dtoList = response.body() ?: emptyList()
        // Transformación a tu modelo Account
        return dtoList.map { dto ->
            Account(
                id       = dto.cuenta.toString(),
                name     = dto.descripcionProducto,
                number   = dto.cuenta ?: "",
                balance  = dto.saldo,
                currency = dto.moneda,
                type     = dto.tipoProducto.trim(),
                montoOtorgado  = dto.montoOtorgado ?: 0.0,
                fechaProx     = dto.fechaProx.toString(),
                movements = dto.movimientos.map { m ->
                    Movement(
                        id             = UUID.randomUUID().toString(),
                        amount         = m.pagoAmort ?: m.importe ?: m.valor ?: 0.0,
                        description    = m.glosa,
                        date           = m.fecha,             // <-- aquí
                        type           = MovementType.PAYMENT, // mapea según tu lógica
                        location       = null,
                        currencySymbol = dto.moneda
                    )
                }
            )
        }
    }

    /** 2) Cabecera de la cuenta */
    suspend fun getAccountInfo(tipo: Int, cuenta: String): AccountDetailDto {
        val token = tokenManager.getToken() ?: throw Exception("No autenticado")

        val resp = api.getAccountInfo("Bearer $token", tipo, cuenta)
        if (!resp.isSuccessful || resp.body() == null) {
            throw Exception(resp.errorBody()?.string() ?: "Error detalle cuenta")
        }
        return resp.body()!!
    }

    /** 3) Movimientos paginados */

    suspend fun getMovements(
        tipo: Int,
        cuenta: String,
        page: Int,
        pageSize: Int
    ): PagedMovementsResponse {
        val token = tokenManager.getToken() ?: throw Exception("No autenticado")
        val resp = api.getMovements("Bearer $token", tipo, cuenta, page, pageSize)
        if (!resp.isSuccessful || resp.body() == null) {
            throw HttpException(resp)
        }
        return resp.body()!!
    }
}