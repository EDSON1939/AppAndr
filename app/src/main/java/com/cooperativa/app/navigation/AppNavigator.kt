package com.cooperativa.app.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cooperativa.app.ui.screens.LoginScreen
import com.cooperativa.app.ui.screens.SplashScreen2
import com.cooperativa.app.viewmodel.AuthViewModel
import com.cooperativa.app.data.local.TokenManager
import kotlinx.coroutines.delay


@Composable
fun AppNavigator(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    tokenManager: TokenManager
) {
    NavHost(
        navController = navController,
        startDestination = "loading"
    ) {
        // Pantalla de carga para decidir el destino inicial
        composable("loading") {
            LaunchedEffect(Unit) {
                Log.d("Navigation", "📌 Ejecutando loading")
                val tokenStillValid = tokenManager.isTokenValid()
                val destination = if (tokenStillValid) "transactions" else "login"
                navController.navigate(destination) {
                    popUpTo("loading") { inclusive = true }
                }
            }
        }

        // Pantalla de Login
        composable("login") {
            Log.d("Navigation", "📌 Pantalla de Login activada")
            LoginScreen(authViewModel) {
                // Al iniciar sesión exitosamente, navega a transactions
                if (tokenManager.isTokenValid()) {
                    navController.navigate("transactions") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        }

        composable("transactions") {
            Log.d("Navigation", "📌 Pantalla de Transacciones activada")
            SplashScreen2() // O renómbrala a TransactionsScreen si lo prefieres

            // Bandera para evitar múltiples navegaciones
            var navigated by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                while (tokenManager.isTokenValid() && !navigated) {
                    delay(5000)
                }
                if (!tokenManager.isTokenValid() && !navigated) {
                    navigated = true
                    Log.d("TokenExpired", "⚠️ Token ha expirado, redirigiendo a login...")
                    tokenManager.clearToken()
                    navController.navigate("login") {
                        // Aquí usamos "loading" para limpiar toda la pila y evitar que la pantalla de transacciones siga activa
                        popUpTo("loading") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

    }
}