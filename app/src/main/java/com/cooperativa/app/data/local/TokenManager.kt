package com.cooperativa.app.data.local

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import org.json.JSONObject

// Extensión para crear DataStore en el Context
val Context.dataStore by preferencesDataStore(name = "token_prefs")

class TokenManager(private val context: Context) {

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val EXPIRATION_TIME = longPreferencesKey("expiration_time")
    }

    // Guarda el token y su tiempo de expiración en DataStore.
    suspend fun saveToken(token: String) {


        val expirationTime = extractExpirationTime(token)
        Log.d("TokenCheck", "Token Time: $expirationTime ")

        //val expirationTime = System.currentTimeMillis() + (20 * 1000L) // Expira en 20 segundos (para pruebas)
        expirationTime?.let {
            context.dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTH_TOKEN] = token
                preferences[PreferencesKeys.EXPIRATION_TIME] = it
            }
        } ?: run {
            Log.e("TokenError", "❌ No se pudo extraer el tiempo de expiración del token.")
        }
    }

    private fun extractExpirationTime(token: String): Long? {
        return try {
            val parts = token.split(".")
            if (parts.size < 3) return null  // El token debe tener 3 partes (header, payload, signature)

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))  // Decodifica el payload
            val jsonObject = JSONObject(payload)
            val exp = jsonObject.getLong("exp")  // Obtiene el campo "exp" (segundos desde 1970)

            exp * 1000  // Convierte segundos a milisegundos
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Recupera el token si aún es válido, de lo contrario devuelve null.
    suspend fun getToken(): String? {
        val preferences = context.dataStore.data.first()
        val expirationTime = preferences[PreferencesKeys.EXPIRATION_TIME] ?: 0L
        return if (System.currentTimeMillis() < expirationTime) {
            preferences[PreferencesKeys.AUTH_TOKEN]
        } else {
            null
        }
    }

    // Devuelve true si el token es válido, false si ya expiró o no existe.
    suspend fun isTokenValid(): Boolean {
        val preferences = context.dataStore.data.first()
        val expirationTime = preferences[PreferencesKeys.EXPIRATION_TIME] ?: 0L
        return System.currentTimeMillis() < expirationTime
    }

    // Obtiene el tiempo de expiración guardado.
    suspend fun getExpirationTime(): Long {
        val preferences = context.dataStore.data.first()
        return preferences[PreferencesKeys.EXPIRATION_TIME] ?: 0L
    }

    // Limpia el token y la expiración de DataStore.
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.AUTH_TOKEN)
            preferences.remove(PreferencesKeys.EXPIRATION_TIME)
        }
    }
}
