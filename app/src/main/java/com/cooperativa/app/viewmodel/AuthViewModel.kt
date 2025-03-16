package com.cooperativa.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cooperativa.app.data.local.TokenManager
import com.cooperativa.app.data.models.LoginResponse
import com.cooperativa.app.data.remote.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


data class LoginState(
    val username: String = "",
    val password: String = "",
    val isPasswordIncorrect: Boolean = false,
    val isLoading: Boolean = false,
    val token: String? = null,
    val errorMessage: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Estado del login
    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState

    // Estado que indica si el token ha expirado
    private val _tokenExpired = MutableStateFlow(false)
    val tokenExpired: StateFlow<Boolean> = _tokenExpired

    // Referencia al Job de verificación del token para evitar fugas
    private var tokenCheckJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (tokenManager.isTokenValid()) {
                startTokenCheck()
            }
        }
    }

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login(onLoginSuccess: () -> Unit) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response: Response<LoginResponse>? =
                    repository.login(_uiState.value.username, _uiState.value.password)
                withContext(Dispatchers.Main) {
                    if (response != null && response.isSuccessful && response.body() != null) {
                        val token = response.body()!!.token
                        tokenManager.saveToken(token)

                        // Reiniciamos el estado de expiración al iniciar sesión exitosamente
                        _tokenExpired.value = false

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            token = token,
                            isPasswordIncorrect = false,
                            errorMessage = null
                        )
                        onLoginSuccess()
                        // Inicia (o reinicia) la verificación del token tras guardar el token nuevo
                        startTokenCheck()
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isPasswordIncorrect = true,
                            errorMessage = response?.message() ?: "Error de conexión"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isPasswordIncorrect = true,
                    errorMessage = "Error de conexión"
                )
            }
        }
    }

    private fun startTokenCheck() {
        // Cancelamos cualquier verificación previa para evitar fugas de memoria
        tokenCheckJob?.cancel()
        tokenCheckJob = viewModelScope.launch(Dispatchers.IO) {
            while (tokenManager.isTokenValid()) {
                // Puedes agregar un log de depuración aquí
                delay(5000)
            }
            tokenManager.clearToken()
            _uiState.value = _uiState.value.copy(token = null)
            _tokenExpired.value = true
        }
    }

    // Esta función ya no se usa directamente en la UI (es suspend) pero se deja para referencia
    suspend fun isAuthenticated(): Boolean = tokenManager.isTokenValid()
}