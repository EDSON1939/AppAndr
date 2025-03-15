package com.cooperativa.app.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")


class TokenManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("token_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val expirationTime = System.currentTimeMillis() + (20 * 1000L) // Expira en 20 segundos
        prefs.edit().putString("auth_token", token).apply()
        prefs.edit().putLong("expiration_time", expirationTime).apply()
        // Log para confirmar la hora de expiración
        android.util.Log.d("TokenManager", "Token guardado: $token")

        android.util.Log.d("TokenManager", "Token guardado. Expira en: $expirationTime")
    }

    fun getToken(): String? {
        val token = prefs.getString("auth_token", null)
        val expirationTime = prefs.getLong("expiration_time", 0L)
        return if (System.currentTimeMillis() < expirationTime) token else null
    }

    fun isTokenValid(): Boolean {

        android.util.Log.d("TokenManager", "Token isTokenValid funcion llamada")

        val expirationTime = prefs.getLong("expiration_time", 0L)
        return System.currentTimeMillis() < expirationTime
    }

    fun getExpirationTime(): Long {
        android.util.Log.d("TokenManager", "Token getExpirationTime funcion llamada")

        return prefs.getLong("expiration_time", 0L)
    }

    fun clearToken() {
        prefs.edit().remove("auth_token").apply()
        prefs.edit().remove("expiration_time").apply()
    }
}

/*
class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "auth_token"
        private const val EXPIRATION_KEY = "token_expiration"
    }

    fun saveToken(token: String, expirationTime: Long) {
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.putLong("expiration", System.currentTimeMillis() + expirationTime) // 🔹 Guarda tiempo de expiración
        editor.apply()
    }

    fun getToken(): String? {
        return if (isTokenValid()) {
            prefs.getString("token", null)
        } else {
            null
        }
    }

    fun isTokenValid(): Boolean {
        val expiration = prefs.getLong("expiration", 0)
        return System.currentTimeMillis() < expiration // ✅ Verifica si aún no ha expirado
    }

    fun getExpirationTime(): Long {
        return sharedPreferences.getLong(EXPIRATION_KEY, 0)
    }


    fun clearToken() {
        prefs.edit().clear().apply()
    }
}*/