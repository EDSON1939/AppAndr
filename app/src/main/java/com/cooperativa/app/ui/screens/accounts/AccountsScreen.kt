package com.cooperativa.app.ui.screens.accounts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.Movement
import com.cooperativa.app.ui.navigation.BottomNavigationBar
import com.cooperativa.app.ui.screens.accounts.components.AccountCard
import com.cooperativa.app.ui.screens.accounts.components.MovementItem
import com.cooperativa.app.ui.theme.NegativeRed
import com.cooperativa.app.ui.theme.PositiveGreen
import com.cooperativa.app.ui.theme.PrimaryBlue
import com.cooperativa.app.ui.theme.SecondaryBlue
import com.cooperativa.app.ui.theme.TextPrimary
import com.cooperativa.app.ui.theme.TextSecondary


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
    var selectedTab by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            MinimalTopBarWithAvatar(
                onNotificationsClick = { /* TODO */ },
                onUserAvatarClick = { /* TODO */ }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (uiState) {
                is AccountsUiState.Loading -> CenterLoading()
                is AccountsUiState.Success -> {
                    val accounts = (uiState as AccountsUiState.Success).data.accounts
                    val filteredAccounts = when(selectedTab) {
                        0 -> accounts.filter { it.type == "AHORRO" }
                        1 -> accounts.filter { it.type == "CREDITO" }
                        2 -> accounts.filter { it.type == "APORTE" }
                        else -> accounts
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(filteredAccounts) { account ->
                            ElevatedAccountCard(
                                account = account,
                                isExpanded = selectedAccountId == account.id,
                                onClick = {
                                    selectedAccountId = if (selectedAccountId == account.id) null else account.id
                                }
                            )

                            if (selectedAccountId == account.id) {
                                Spacer(modifier = Modifier.height(8.dp))
                                RecentTransactionsSection(
                                    account = account,
                                    onViewAllClick = {
                                        navController.navigate("movements/${account.id}")
                                    }
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
                is AccountsUiState.Error -> ErrorScreen(message = (uiState as AccountsUiState.Error).message)
            }
        }
    }
}



@Composable
fun ElevatedAccountCard(
    account: Account,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isExpanded) 8.dp else 4.dp),
        border = if (isExpanded) BorderStroke(1.dp, PrimaryBlue.copy(alpha = 0.5f)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = account.number,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Text(
                    text = "${account.balance} ${account.currency}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Toca para cerrar",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun RecentTransactionsSection(
    account: Account,
    onViewAllClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Encabezado unificado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos Movimientos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                TextButton(
                    onClick = onViewAllClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryBlue
                    )
                ) {
                    Text("Ver todos")
                }
            }

            // Línea divisoria
            Divider(
                color = Color.LightGray.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Lista de movimientos
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                account.movements.take(5).forEachIndexed { index, movement ->
                    if (index > 0) {
                        Divider(
                            color = Color.LightGray.copy(alpha = 0.1f),
                            thickness = 1.dp
                        )
                    }
                    TransactionItem(movement = movement)
                }
            }

            // Espacio inferior
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun TransactionItem(movement: Movement) {
    val isPositive = movement.amount > 0
    val amountText = "${if (isPositive) "+" else "-"} ${movement.amount} $$$"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = movement.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                movement.location?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(
                    text = movement.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Text(
            text = amountText,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (isPositive) PositiveGreen else NegativeRed
        )
    }
}

@Composable
fun TransactionCard(movement: Movement) {
    val isPositive = movement.amount > 0
    val icon = if (isPositive) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward
    val iconColor = if (isPositive) PositiveGreen else NegativeRed
    val amountText = "${if (isPositive) "+" else "-"} ${movement.amount} $$$"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (isPositive) "Ingreso" else "Egreso",
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movement.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movement.location ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text(
                    text = movement.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }

            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = if (isPositive) PositiveGreen else NegativeRed
            )
        }
    }
}

@Composable
fun UltimosMovimientosSection(
    account: Account,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        // Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ÚLTIMOS MOVIMIENTOS",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF081B61)
            )
            TextButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF081B61)
                )
            ) {
                Text("VER TODO")
            }
        }

        // Tarjeta de movimientos
        UltimosMovimientosCard(
            movements = account.movements.take(5),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun UltimosMovimientosCard(
    movements: List<Movement>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            movements.forEachIndexed { index, movement ->
                if (index > 0) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
                MovementItem(movement = movement)
            }
        }
    }
}

@Composable
fun FancyAccountCard(
    account: Account,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF081B61)
        ),
        elevation = CardDefaults.cardElevation(if (isExpanded) 8.dp else 4.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
        ) {
            // Fondo con onda (de tu versión anterior)
            Canvas(modifier = Modifier.matchParentSize()) {
                val width = size.width
                val height = size.height
                val path = Path().apply {
                    moveTo(width, 0f)
                    cubicTo(
                        width * 0.6f, 0f,
                        width * 0.4f, height * 0.4f,
                        0f, height
                    )
                    lineTo(width, height)
                    close()
                }
                drawPath(path = path, color = Color(0xFF081B61).copy(alpha = 0.9f))
            }

            // Contenido de la cuenta
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = account.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "CUENTA DE ${account.name}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "SALDO ${account.balance} ${account.currency}",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "NR ${account.number}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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