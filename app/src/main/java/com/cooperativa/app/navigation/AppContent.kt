package com.cooperativa.app.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.cooperativa.app.viewmodel.AuthViewModel

@Composable
fun AppContent(navController: NavHostController, authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        Log.d("AppContent", "AuthViewModel uiState: $uiState")
    }

    // Observa el estado de expiración del token
    val tokenExpired by authViewModel.tokenExpired.collectAsState()
    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(tokenExpired) {
        if (tokenExpired && !navigated) {
            navigated = true
            Log.d("TokenExpired", "⚠️ Token expirado: navegando a login")
            // Eliminamos la pantalla de transactions para evitar que se cargue de nuevo
            navController.navigate("login") {
                popUpTo("transactions") { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    AppNavigator(navController = navController, authViewModel = authViewModel)
}
