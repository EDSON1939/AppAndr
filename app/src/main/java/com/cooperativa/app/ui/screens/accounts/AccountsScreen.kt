package com.cooperativa.app.ui.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.ui.screens.accounts.components.AccountList
import com.cooperativa.app.ui.screens.accounts.components.MovementList
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsUiState
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModel
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.ui.screens.accounts.components.AccountCard
import com.cooperativa.app.ui.screens.accounts.components.MovementItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountAndMovements(
    accountsViewModelFactory: AccountsViewModelFactory,
    onLogout: () -> Unit,
    navController: NavController,
    viewModel: AccountsViewModel = viewModel(factory = accountsViewModelFactory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedAccount by viewModel.selectedAccount.collectAsState()

    Scaffold(
        topBar = {
            MinimalTopBarWithAvatar(
                onNotificationsClick = { /* TODO */ },
                onUserAvatarClick = { /* TODO */ }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                is AccountsUiState.Loading -> CenterLoading()
                is AccountsUiState.Success -> {
                    val data = (uiState as AccountsUiState.Success).data

                    if (selectedAccount != null) {
                        AccountDetailScreen(
                            account = selectedAccount!!,
                            onViewAllClick = { /* Navegar a pantalla completa */ },
                            onBackClick = { viewModel.selectAccount(null) }
                        )
                    } else {
                        AccountListScreen(
                            accounts = data.accounts,
                            onAccountSelected = { viewModel.selectAccount(it) }
                        )
                    }
                }
                is AccountsUiState.Error -> {
                    ErrorScreen(message = (uiState as AccountsUiState.Error).message)
                }
            }
        }
    }
}

@Composable
private fun AccountListScreen(
    accounts: List<Account>,
    onAccountSelected: (Account) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(accounts) { account ->
            AccountCard(
                account = account,
                onClick = { onAccountSelected(account) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AccountDetailScreen(
    account: Account,
    onViewAllClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = account.number,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        Text(
            text = "Saldo: ${account.balance} ${account.currency}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Últimos movimientos",
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = onViewAllClick) {
                Text("Ver todos")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(account.movements.take(5)) { movement ->
                MovementItem(movement)
                Divider()
            }
        }
    }
}

@Composable
private fun CenterLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimalTopBarWithAvatar(
    onNotificationsClick: () -> Unit,
    onUserAvatarClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        title = {
            Column {
                Text(
                    text = "COOPERATIVA APP",
                    color = Color(0xFF081B61),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                )
                Text(
                    text = "Barly Vallendito",
                    color = Color(0xFF081B61),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color(0xFF081B61),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onUserAvatarClick) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Avatar de usuario",
                    tint = Color(0xFF081B61),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}