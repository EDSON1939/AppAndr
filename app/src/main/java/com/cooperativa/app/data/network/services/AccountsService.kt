package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.AccountDetailDto
import com.cooperativa.app.data.models.AccountResponse
import com.cooperativa.app.data.models.MovementDto
import com.cooperativa.app.data.models.PagedMovementsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AccountsService {
    @GET("AccAndMov/accounts/{tipo}")
    suspend fun getAccounts(
        @Header("Authorization") token: String,
        @Path("tipo") tipo: Int
    ): Response<List<AccountResponse>>

    // cabecera de la cuenta
    @GET("AccAndMov/accountsDetail/{tipo}/{cuenta}")
    suspend fun getAccountInfo(
        @Header("Authorization") token: String,
        @Path("tipo") tipo: Int,
        @Path("cuenta") cuenta: String
    ): Response<AccountDetailDto>

    // movimientos paginados
    @GET("AccAndMov/accountsDetailMov/{tipo}/{cuenta}/movements")
    suspend fun getMovements(
        @Header("Authorization") token: String,
        @Path("tipo") tipo: Int,
        @Path("cuenta") cuenta: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10
    ): Response<PagedMovementsResponse>
}