package com.cooperativa.app.ui.screens.accounts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.ui.navigation.BottomNavigationBar
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
    var selectedAccountId by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableStateOf(1) } // 1=Créditos (según tu mockup)

    Scaffold(
        topBar = {
            MinimalTopBarWithAvatar(
                onNotificationsClick = { /* TODO */ },
                onUserAvatarClick = { /* TODO */ }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedTab) { tabIndex ->
                selectedTab = tabIndex
                // Aquí puedes agregar navegación si es necesario
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (uiState) {
                is AccountsUiState.Loading -> CenterLoading()
                is AccountsUiState.Success -> {
                    val accounts = (uiState as AccountsUiState.Success).data.accounts

                    // Filtra cuentas según la pestaña seleccionada
                    val filteredAccounts = when(selectedTab) {
                        0 -> accounts.filter { it.type == "AHORRO" }
                        1 -> accounts.filter { it.type == "CREDITO" }
                        2 -> accounts.filter { it.type == "APORTE" }
                        else -> accounts
                    }

                    // Lista vertical de cuentas filtradas
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(filteredAccounts) { account ->
                            AccountItem(
                                account = account,
                                isExpanded = selectedAccountId == account.id,
                                onAccountClick = {
                                    selectedAccountId = if (selectedAccountId == account.id) null else account.id
                                },
                                onViewAllClick = {
                                    navController.navigate("movements/${account.id}")
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                is AccountsUiState.Error -> ErrorScreen(message = (uiState as AccountsUiState.Error).message)
            }
        }
    }
}

@Composable
fun AccountItem(
    account: Account,
    isExpanded: Boolean,
    onAccountClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    val transition = updateTransition(isExpanded, label = "accountTransition")
    val cardElevation by transition.animateDp(label = "elevation") { if (it) 8.dp else 2.dp }
    val cardColor by transition.animateColor(label = "color") {
        if (it) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(cardElevation),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column {
            // Encabezado de la cuenta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAccountClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = account.number,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "${account.balance} ${account.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Contenido expandible
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Encabezado movimientos
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ÚLTIMOS MOVIMIENTOS",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(onClick = onViewAllClick) {
                            Text("VER TODOS")
                        }
                    }

                    // Lista de movimientos (5 primeros)
                    Column {
                        account.movements.take(5).forEach { movement ->
                            MovementItem(movement = movement)
                            Divider(thickness = 0.5.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
/*
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
*/
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
 fun CenterLoading() {
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