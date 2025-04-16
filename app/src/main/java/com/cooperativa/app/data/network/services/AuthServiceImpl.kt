package com.cooperativa.app.data.network.services

import android.util.Log
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.models.LoginRequest
import com.cooperativa.app.data.models.LoginResponse
import com.cooperativa.app.data.util.Result
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val api: AuthService,
    private val tokenManager: TokenManager
) {
    suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            Log.e("Login", "Servioce")

            val response = api.login(LoginRequest(username, password))
            Log.e("Login", "$response")

            if (response.isSuccessful && response.body() != null) {
                // Guarda el token recibido
                tokenManager.saveToken(response.body()!!.token)
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


    ///CAMBIO DE CONTRASEÑA


    suspend fun checkFirstLogin(token: String): Boolean {
        Log.e("Login", "checkFirstLogin")

        val response = api.checkFirstLogin("Bearer $token")
        Log.e("Login", "$response")

        return if (response.isSuccessful) {
            response.body()?.isFirstLogin ?: false
        } else {
            false // O maneja el error según tu lógica
        }
    }
    suspend fun changePassword(currentPassword: String, newPassword: String,token: String): Result<Unit> {
        return try {
            // Crea el objeto request correctamente
            val request = ChangePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword
            )

            val response = api.changePassword("Bearer $token",request) // Pasa el objeto request
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Failure(Exception("Password change failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}


