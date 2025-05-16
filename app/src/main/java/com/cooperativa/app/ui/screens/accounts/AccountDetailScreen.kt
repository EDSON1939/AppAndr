package com.cooperativa.app.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cooperativa.app.data.models.AccountDetailDto
import com.cooperativa.app.data.models.MovementDto
import com.cooperativa.app.ui.screens.accounts.components.MovementsList
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountDetailUiState
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountDetailViewModel
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountDetailViewModelFactory
import com.cooperativa.app.ui.theme.NegativeRed
import com.cooperativa.app.ui.theme.PositiveGreen
import com.cooperativa.app.ui.theme.PrimaryBlue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailRoute(
    navController: NavController,
    tipo: Int,
    cuenta: String,
    viewModelFactory: AccountDetailViewModelFactory
) {
    val vm: AccountDetailViewModel = viewModel(factory = viewModelFactory)
    val uiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) { vm.loadDetail(tipo, cuenta) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cuenta") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is AccountDetailUiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                is AccountDetailUiState.Error -> {
                    Text(
                        text = (uiState as AccountDetailUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is AccountDetailUiState.Success -> {
                    val state = uiState as AccountDetailUiState.Success
                    Column(Modifier.fillMaxSize()) {
                        AccountHeader(detail = state.detail)
                        Spacer(Modifier.height(8.dp))
                        LazyColumn(Modifier.weight(1f)) {
                            items(state.movements) { m ->
                                DetailMovementItem(m)
                                Divider()
                            }
                        }
                        PaginationBar(
                            currentPage = vm.currentPageNumber(),
                            totalPages = state.totalPages,
                            onPageSelected = { vm.loadMovementsPage(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AccountHeader(detail: AccountDetailDto) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            .padding(16.dp)
    ) {
        Text(detail.tipoProducto, color = Color.White)
        Spacer(Modifier.height(4.dp))
        Text("NR CUENTA ${detail.cuenta}", color = Color.White, fontSize = 18.sp)
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("Monto a Otrogado  :", color = Color.White.copy(alpha = 0.7f))
                Text("${detail.montoOtorgado} ${detail.moneda}", color = Color.White)
            }
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text("Saldo:", color = Color.White.copy(alpha = 0.7f))
                Text("${detail.saldo} ${detail.moneda}", color = Color.White)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit,
    maxButtons: Int = 5
) {
    // Construye lista de páginas + “…” (representado con -1)
    val pages = remember(currentPage, totalPages) {
        if (totalPages <= maxButtons + 2) {
            (1..totalPages).toList()
        } else {
            val half = maxButtons / 2
            val start = (currentPage - half).coerceAtLeast(2)
            val end = (currentPage + half).coerceAtMost(totalPages - 1)
            buildList {
                add(1)
                if (start > 2) add(-1)
                addAll(start..end)
                if (end < totalPages - 1) add(-1)
                add(totalPages)
            }
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        pages.forEach { p ->
            if (p == -1) {
                Text("...", modifier = Modifier.padding(horizontal = 4.dp))
            } else {
                val selected = p == currentPage
                Button(
                    onClick = { onPageSelected(p) },
                    enabled = !selected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.White,
                        contentColor  = if (selected) Color.White else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("$p")
                }
            }
        }
    }
}

@Composable
fun DetailMovementItem(m: MovementDto) {
    val amount = m.importe ?: m.pagoAmort ?: m.valor ?: 0.0
    val signo = if (amount > 0) "+" else "-"
    val color = if (amount > 0) PositiveGreen else NegativeRed

    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(m.glosa, fontWeight = FontWeight.Bold)
        Column(horizontalAlignment = Alignment.End) {
            Text("$signo ${"%.2f".format(amount)}", color = color, fontWeight = FontWeight.Bold)
            Text(m.fecha, fontSize = 12.sp, color = Color.Gray)
        }
    }
}