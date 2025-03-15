package com.cooperativa.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cooperativa.app.data.local.TokenManager
import com.cooperativa.app.data.remote.AuthRepository

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager // ✅ Agregado el TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}