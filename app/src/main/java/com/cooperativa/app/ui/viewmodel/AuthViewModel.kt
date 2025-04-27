package com.cooperativa.app.ui.viewmodel

import android.util.Log
import com.cooperativa.app.data.managers.DeviceInfoHelper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.network.services.AuthServiceImpl
import com.cooperativa.app.data.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authService: AuthServiceImpl,
    private val tokenManager: TokenManager,
    private val deviceInfoHelper: DeviceInfoHelper

) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChanged(username: String) {
        _uiState.value = _uiState.value.copy(username = username)
    }

    fun onPasswordChanged(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isPasswordIncorrect = false,
                errorMessage = null
            )

            //////// AQUI OBTENER TODO LO DE DEVICEHELPER

            val deviceInfo = mapOf(
                "deviceName" to deviceInfoHelper.getDeviceName(),
                "deviceModel" to deviceInfoHelper.getDeviceModel(),
                "androidId" to deviceInfoHelper.getAndroidId(),
                "macAddress" to deviceInfoHelper.getMacAddress(),
                "ipAddress" to deviceInfoHelper.getIpAddress(),
                "stableDeviceId" to deviceInfoHelper.getStableDeviceId(),
                "ID" to deviceInfoHelper.getDeviceId()
            )

            when (val result = authService.login(
                _uiState.value.username,
                _uiState.value.password,
                deviceInfo
            )) {


                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true
                    )
                }
                is Result.Failure -> {
                    Log.d("login", "Datos  $deviceInfo")

                    _passwordCreationState.value = _passwordCreationState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Error al cambiar contraseña")






                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isPasswordIncorrect = result.exception.message == "password_incorrect",
                        errorMessage = result.exception.message ?: "Error Interno"
                    )
                }
            }
        }
    }

    suspend fun getToken(): String? {
        return tokenManager.getToken() // Asumiendo que tienes un TokenManager inyectado
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearToken()
            _uiState.value = LoginUiState()
        }
    }

    ///CAMBIO CONTRASEÑA

    // Función para actualizar contraseña repetida
    fun onRepeatPasswordChanged(repeatPassword: String) {
        _passwordCreationState.value = _passwordCreationState.value.copy(
            repeatPassword = repeatPassword
        )
    }

    fun onNewPasswordChanged(newPassword: String) {
        _passwordCreationState.value = _passwordCreationState.value.copy(
            newPassword = newPassword
        )
    }
    fun onCurrentPasswordChanged(currentPassword: String) {
        _passwordCreationState.value = _passwordCreationState.value.copy(
            currentPassword = currentPassword
        )
    }
    suspend fun checkFirstLogin(token: String): Boolean {
        return authService.checkFirstLogin(token)
    }

    // Estado para creación de contraseña
    private val _passwordCreationState = MutableStateFlow(PasswordCreationState())
    val passwordCreationState: StateFlow<PasswordCreationState> = _passwordCreationState

    // Función para verificar primer inicio


    // Función para cambiar contraseña
    fun changePassword(
        currentPassword: String,
        newPassword: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _passwordCreationState.value = _passwordCreationState.value.copy(isLoading = true)

            // Obtenemos el token desde el TokenManager
            val token = tokenManager.getToken() ?: run {
                _passwordCreationState.value = _passwordCreationState.value.copy(
                    isLoading = false,
                    errorMessage = "Sesión no válida"
                )
                return@launch
            }

            when (val result = authService.changePassword(
                currentPassword = currentPassword,
                newPassword = newPassword,
                token = token
            )) {
                is Result.Success -> {
                    _passwordCreationState.value = _passwordCreationState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                    onSuccess()
                }
                is Result.Failure -> {
                    _passwordCreationState.value = _passwordCreationState.value.copy(
                        isLoading = false,
                        errorMessage = result.exception.message ?: "Error al cambiar contraseña"
                    )
                }
            }
        }
    }

}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val token: String? = null,
    val isPasswordIncorrect: Boolean = false
)
data class PasswordCreationState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val repeatPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)