package com.cooperativa.app.ui.screens.accounts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AccountsServiceImpl
import javax.inject.Inject

class AccountDetailViewModelFactory @Inject constructor(
    private val service: AccountsServiceImpl,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccountDetailViewModel(service, tokenManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}