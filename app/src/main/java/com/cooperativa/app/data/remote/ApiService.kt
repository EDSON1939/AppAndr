package com.cooperativa.app.data.remote

import com.cooperativa.app.data.models.LoginRequest
import com.cooperativa.app.data.models.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService { // <-- ASEGÚRATE DE QUE SEA ApiService
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
