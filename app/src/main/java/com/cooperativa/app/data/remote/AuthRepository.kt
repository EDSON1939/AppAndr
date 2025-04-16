package com.cooperativa.app.data.remote

import com.cooperativa.app.data.models.LoginRequest
import com.cooperativa.app.data.models.LoginResponse
import retrofit2.Response
import android.util.Log

class AuthRepository(private val api: ApiService) {

    suspend fun login(username: String, password: String): Response<LoginResponse>? {
        Log.d("login", "Datos Time: $username  +  $password")

        val request = LoginRequest(username, password) // ✅ Crear el objeto antes de enviarlo

        Log.d("login", "Datos  $request")

        val apitest = api.login(request)

        Log.d("login", "Datos  $apitest")

        return apitest // ✅ Pasarlo como argumento
    }
}