package com.cooperativa.app.ui.screens.accounts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cooperativa.app.data.managers.TokenManager
import com.cooperativa.app.data.models.AccountDetailDto
import com.cooperativa.app.data.models.MovementDto
import com.cooperativa.app.data.models.PagedMovementsResponse
import com.cooperativa.app.data.network.services.AccountsService
import com.cooperativa.app.data.network.services.AccountsServiceImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class AccountDetailUiState {
    object Loading : AccountDetailUiState()
    data class Success(
        val detail: AccountDetailDto,
        val movements: List<MovementDto>,
        val totalPages: Int
    ) : AccountDetailUiState()
    data class Error(val message: String) : AccountDetailUiState()
}

class AccountDetailViewModel @Inject constructor(
    private val service: AccountsServiceImpl,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<AccountDetailUiState>(AccountDetailUiState.Loading)
    val uiState: StateFlow<AccountDetailUiState> = _uiState
    private var tipoGlobal: Int = 1
    private var currentPage = 1
    private val pageSize = 5
    private var lastDetail: AccountDetailDto? = null

    fun loadDetail(tipo: Int, cuenta: String) {
        tipoGlobal = tipo
        viewModelScope.launch {
            _uiState.value = AccountDetailUiState.Loading
            try {
                // 1) Info de cuenta
                val detail = service.getAccountInfo(tipo, cuenta)
                lastDetail = detail

                // 2) Página 1 de movimientos
                val paged: PagedMovementsResponse = service.getMovements(tipo, cuenta, 1, pageSize)
                currentPage = 1

                _uiState.value = AccountDetailUiState.Success(
                    detail       = detail,
                    movements    = paged.movements,
                    totalPages   = paged.totalPages
                )
            } catch (e: Exception) {
                _uiState.value = AccountDetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun loadMovementsPage(page: Int) {
        val detail = lastDetail ?: return
        viewModelScope.launch {
            _uiState.value = AccountDetailUiState.Loading
            try {
                val paged = service.getMovements(
                    tipo     = tipoGlobal,           // el tipo real ya lo usaste en loadDetail
                    cuenta   = detail.cuenta,
                    page     = page,
                    pageSize = pageSize
                )
                currentPage = page
                _uiState.value = AccountDetailUiState.Success(
                    detail     = detail,
                    movements  = paged.movements,
                    totalPages = paged.totalPages
                )
            } catch (e: Exception) {
                _uiState.value = AccountDetailUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun currentPageNumber() = currentPage
}

