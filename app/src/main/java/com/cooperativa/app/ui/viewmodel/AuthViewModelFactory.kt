package com.cooperativa.app.ui.viewmodel

import com.cooperativa.app.data.managers.DeviceInfoHelper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cooperativa.app.data.network.services.AuthServiceImpl
import com.cooperativa.app.data.managers.TokenManager
import javax.inject.Inject

class AuthViewModelFactory @Inject constructor(
    private val authServiceImpl: AuthServiceImpl,
    private val tokenManager: TokenManager,
    private val deviceInfoHelper: DeviceInfoHelper

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authServiceImpl, tokenManager, deviceInfoHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
