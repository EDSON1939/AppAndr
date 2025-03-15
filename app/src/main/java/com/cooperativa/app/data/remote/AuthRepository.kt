package com.cooperativa.app.data.remote

import com.cooperativa.app.data.models.LoginRequest
import com.cooperativa.app.data.models.LoginResponse
import retrofit2.Response
import android.util.Log
import com.cooperativa.app.data.local.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val api: ApiService) {

    suspend fun login(username: String, password: String): Response<LoginResponse>? {
        val request = LoginRequest(username, password) // ✅ Crear el objeto antes de enviarlo
        return api.login(request) // ✅ Pasarlo como argumento
    }
}