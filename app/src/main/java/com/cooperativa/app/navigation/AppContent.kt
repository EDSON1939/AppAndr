package com.cooperativa.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.cooperativa.app.ui.screens.MainContainerScreen
import com.cooperativa.app.viewmodel.AuthViewModel

@Composable
fun AppContent(navController: NavHostController, authViewModel: AuthViewModel) {
    val tokenExpired by authViewModel.tokenExpired.collectAsState()
    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(tokenExpired) {
        if (tokenExpired && !navigated) {
            navigated = true
            navController.navigate("login") {
                // Por ejemplo, hacemos popUpTo la ruta "ahorros" para limpiar la pila.
                popUpTo("ahorros") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Pasamos el mismo navController a MainContainerScreen
    MainContainerScreen(authViewModel, navController)
}

















/*
@Composable
fun AppContent(navController: NavHostController, authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    // Para depuración, puedes mantener un log (o eliminarlo en producción)
    // LaunchedEffect(uiState) { Log.d("AppContent", "AuthViewModel uiState: $uiState") }

    val tokenExpired by authViewModel.tokenExpired.collectAsState()
    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(tokenExpired) {
        if (tokenExpired && !navigated) {
            navigated = true
            navController.navigate("login") {
                popUpTo("transactions") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    AppNavigator(navController = navController, authViewModel = authViewModel)
}
*/