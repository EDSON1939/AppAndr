package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.models.Account
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AccountsService {
    @GET("account/GetAccountsMovements")
    suspend fun getAccounts(
        @Header("Authorization") token: String
    ): Response<List<Account>>
}