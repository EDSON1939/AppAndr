package com.cooperativa.app.ui.screens
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cooperativa.app.ui.navigation.AppNavigator
import com.cooperativa.app.ui.screens.components.BottomNavigationBar
import com.cooperativa.app.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainerScreen(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showTopBar = currentRoute != "login"
    val showBottomBar = currentRoute != "login"

    Scaffold(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            if (showTopBar) {
                // Barra superior minimalista
                MinimalTopBarWithAvatar(
                    onNotificationsClick = { /* Ir a notificaciones */ },
                    onUserAvatarClick = { /* Ir a perfil */ }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    tokenExpired = authViewModel.tokenExpired.collectAsState().value
                )
            }
        }
    ) { innerPadding ->
        // Columna que contiene el contenido principal (AppNavigator)
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Un espacio para que el contenido no quede pegado a la top bar
            Spacer(modifier = Modifier.height(12.dp))

            // Navegación principal
            AppNavigator(
                navController = navController,
                authViewModel = authViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

/**
 * Top bar minimalista: "COOPERATIVA APP" / "Barly Vallendito" + íconos de notificaciones y avatar
 */
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
*/