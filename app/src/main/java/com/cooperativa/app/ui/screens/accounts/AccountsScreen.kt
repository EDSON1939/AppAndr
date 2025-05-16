package com.cooperativa.app.ui.screens.accounts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsUiState
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModel
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.Movement
import com.cooperativa.app.ui.navigation.BottomNavigationBar
import com.cooperativa.app.ui.theme.NegativeRed
import com.cooperativa.app.ui.theme.PositiveGreen
import com.cooperativa.app.ui.theme.PrimaryBlue
import com.cooperativa.app.ui.theme.TextPrimary
import com.cooperativa.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay

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


    fun tabToTipo(tab: Int): Int = when(tab) {
        0 -> 1  // AHORRO
        1 -> 2  // CREDITO
        2 -> 3  // TODOS (o 3 si quisieras CERTIFICADO)
        else -> 0
    }
    LaunchedEffect(selectedTab) {
        viewModel.loadAccounts(tabToTipo(selectedTab))
    }

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
                        2 -> accounts.filter { it.type == "CERTIFICADO" }
                        else -> accounts
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(filteredAccounts) { account ->
                            ModernAccountCard(
                                account = account,
                                isExpanded = selectedAccountId == account.id,
                                onClick = {
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





@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModernAccountCard(
    account: Account,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFF081B61) else Color.White,
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )
    val textColor = if (isExpanded) Color.White else Color(0xFF081B61)
    val subTextColor = if (isExpanded) Color.White.copy(alpha = 0.6f) else Color.Gray
    val orangeSoft = Color(0xFFFFA726)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { onClick() },
        color = backgroundColor,
        shape = RoundedCornerShape(20.dp),
        shadowElevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Fila 1: Descripción
            Text(
                text = account.name.uppercase(),
                fontSize = 13.sp,
                color = subTextColor
            )

            Spacer(Modifier.height(4.dp))

            // Fila 2: Número de cuenta
            Text(
                text = account.number,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            Spacer(Modifier.height(12.dp))

            if (account.type == "CREDITO") {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // IZQUIERDA: Monto otorgado
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Monto otorgado:",
                            fontSize = 12.sp,
                            color = subTextColor
                        )
                        Text(
                            text = "${account.currency} ${account.montoOtorgado}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }

                    // DERECHA: Saldo Deuda
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "Saldo",
                            fontSize = 12.sp,
                            color = subTextColor
                        )
                        Text(
                            text = "Bs. ${account.balance}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }


                Spacer(Modifier.height(8.dp))

                // Fila 4: Fecha próximo pago | Próximo monto a pagar
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Fecha próximo pago: ${account.fechaProx}",
                        fontSize = 12.sp,
                        color = subTextColor,
                        modifier = Modifier.weight(1f)
                    )

                }

            } else {
                // --- PARA AHORRO / APORTE ---

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Saldo",
                            fontSize = 12.sp,
                            color = subTextColor
                        )
                        Text(
                            text = "Bs. ${account.balance}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            // --- Detalle expandible con movimientos ---
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(16.dp))
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Últimos Movimientos",
                            color = textColor,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = onViewAllClick,
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = orangeSoft.copy(alpha = 0.9f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(2.dp),
                            border = BorderStroke(1.dp, orangeSoft.copy(alpha = 0.7f))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Ver todos", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    account.movements.take(5).forEachIndexed { i, m ->
                        if (i > 0) Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = m.description,
                                    color = textColor,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = m.date,
                                    fontSize = 11.sp,
                                    color = subTextColor
                                )
                            }
                            Text(
                                text = (if (m.amount > 0) "+ " else "- ") + "Bs. ${m.amount}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (m.amount > 0) PositiveGreen else NegativeRed
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

/*
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ModernAccountCard(
    account: Account,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onViewAllClick: () -> Unit
) {
    // Animamos el color de fondo
    val backgroundColor by animateColorAsState(
        targetValue = if (isExpanded) Color(0xFF1976D2) else Color.White,
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )
    val orangeSoft = Color(0xFFFFA726)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = if (isExpanded) 8.dp else 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // — Header clickeable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick() },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        account.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isExpanded) Color.White else Color(0xFF081B61)
                    )
                    Text(
                        account.number,
                        fontSize = 12.sp,
                        color = if (isExpanded) Color.White.copy(alpha = 0.7f) else Color.Gray
                    )
                }
                Text(
                    "Bs. ${account.balance}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (isExpanded) Color.White else Color(0xFF081B61)
                )
            }

            // — Aquí envolvemos TODO el bloque de detalles en AnimatedVisibility
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(tween(400)) +
                        expandVertically(
                            expandFrom = Alignment.Top,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ),
                exit = fadeOut(tween(400)) +
                        shrinkVertically(
                            shrinkTowards = Alignment.Top,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        )
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(Modifier.height(16.dp))
                    Divider(color = Color.White.copy(alpha = 0.3f))
                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Últimos Movimientos",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = onViewAllClick,
                            modifier = Modifier.height(36.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = orangeSoft.copy(alpha = 0.9f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            elevation = ButtonDefaults.buttonElevation(2.dp, pressedElevation = 4.dp),
                            border = BorderStroke(1.dp, orangeSoft.copy(alpha = 0.7f))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Ver todos")
                                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Lista de movimientos
                    Column(modifier = Modifier.fillMaxWidth()) {
                        account.movements.take(5).forEachIndexed { i, m ->
                            if (i > 0) Spacer(Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(m.description, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                                    Text(m.date, fontSize = 11.sp, color = Color.White.copy(alpha = 0.7f))
                                }
                                Text(
                                    (if (m.amount > 0) "+ " else "- ") + "Bs. ${m.amount}",
                                    fontWeight = FontWeight.Bold,
                                    color = if (m.amount > 0) PositiveGreen else NegativeRed
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

*/

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