package com.cooperativa.app.data.network.services

import com.cooperativa.app.data.models.LoginRequest
import com.cooperativa.app.data.models.LoginResponse
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

        @GET("auth/first-login")
        suspend fun checkFirstLogin(
            @Header("Authorization") token: String
        ): Response<FirstLoginResponse>


    @POST("auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<ChangePasswordResponse>
}

///CAMBIAR A CARPETA DTO
data class FirstLoginResponse(
    @SerializedName("isFirstLogin")
    val isFirstLogin: Boolean
)

data class ChangePasswordRequest(
    @SerializedName("currentPassword") val currentPassword: String,
    @SerializedName("newPassword") val newPassword: String
)

data class ChangePasswordResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("error") val error: String?
)