package com.cooperativa.app.ui.screens.accounts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cooperativa.app.data.managers.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AccountsServiceImpl

class AccountsViewModel @Inject constructor(
    private val accountsService: AccountsServiceImpl,
    private val tokenManager: TokenManager

) : ViewModel() {

    private val _uiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val uiState: StateFlow<AccountsUiState> = _uiState

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    val selectedAccount: StateFlow<Account?> = _selectedAccount

    init {
        loadAccounts(1)
    }

    fun loadAccounts(tipo: Int) {
        viewModelScope.launch {
            _uiState.value = AccountsUiState.Loading
            try {
                val accounts = accountsService.getAccounts(tipo)
                _uiState.value = AccountsUiState.Success(
                    AccountsData(
                        accounts = accounts,
                        selectedAccount = _selectedAccount.value
                    )
                )
            } catch (e: Exception) {
                _uiState.value = AccountsUiState.Error(
                    when {
                        e.message?.contains("No autenticado") == true -> "Sesión expirada"
                        else -> e.message ?: "Error al cargar cuentas"
                    }
                )
            }
        }
    }

    fun selectAccount(account: Account?) {
        _selectedAccount.value = account
        // Actualizar el UIState con la nueva cuenta seleccionada
        (_uiState.value as? AccountsUiState.Success)?.let { currentState ->
            _uiState.value = currentState.copy(
                data = currentState.data.copy(selectedAccount = account)
            )
        }
    }
}

sealed class AccountsUiState {
    object Loading : AccountsUiState()
    data class Success(val data: AccountsData) : AccountsUiState() {
        fun copyWithSelectedAccount(account: Account): Success {
            return copy(data = data.copy(selectedAccount = account))
        }
    }
    data class Error(val message: String) : AccountsUiState()
}

data class AccountsData(
    val accounts: List<Account>,
    val selectedAccount: Account?
)