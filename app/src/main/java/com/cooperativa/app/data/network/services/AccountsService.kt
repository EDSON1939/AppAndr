package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.AccountResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface AccountsService {
    @GET("AccAndMov/accounts/{tipo}")
    suspend fun getAccounts(
        @Header("Authorization") token: String,
        @Path("tipo") tipo: Int
    ): Response<List<AccountResponse>>
}
