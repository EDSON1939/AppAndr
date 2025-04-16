package com.cooperativa.app.data.managers

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

private val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "token_prefs")

@Singleton
class TokenManager @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val EXPIRATION_TIME = longPreferencesKey("expiration_time")
    }

    suspend fun saveToken(token: String) {
        extractExpirationTime(token)?.let { exp ->
            context.tokenDataStore.edit { prefs ->
                prefs[PreferencesKeys.AUTH_TOKEN] = token
                prefs[PreferencesKeys.EXPIRATION_TIME] = exp
            }
            Log.d("TokenManager", "Token saved. Expires at: $exp")
        } ?: Log.e("TokenManager", "Failed to extract token expiration")
    }

    val tokenFlow = context.tokenDataStore.data
        .map { prefs ->
            val token = prefs[PreferencesKeys.AUTH_TOKEN]
            val exp = prefs[PreferencesKeys.EXPIRATION_TIME] ?: 0L
            if (token != null && System.currentTimeMillis() < exp) token else null
        }

    suspend fun getToken(): String? {
        return context.tokenDataStore.data
            .map { prefs -> prefs[PreferencesKeys.AUTH_TOKEN] }
            .first()
    }

    suspend fun isTokenValid(): Boolean = getToken() != null

    suspend fun clearToken() {
        context.tokenDataStore.edit { prefs ->
            prefs.remove(PreferencesKeys.AUTH_TOKEN)
            prefs.remove(PreferencesKeys.EXPIRATION_TIME)
        }
        Log.d("TokenManager", "Token cleared")
    }

    private fun extractExpirationTime(token: String): Long? {
        return try {
            val payload = token.split(".")[1]
            val json = JSONObject(String(Base64.decode(payload, Base64.URL_SAFE)))
            json.getLong("exp") * 1000 // Convertir a milisegundos
        } catch (e: Exception) {
            Log.e("TokenManager", "Error parsing token: ${e.message}")
            null
        }
    }
}