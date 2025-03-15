package com.cooperativa.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cooperativa.app.ui.screens.LoginScreen
import com.cooperativa.app.ui.screens.SplashScreen2
import com.cooperativa.app.viewmodel.AuthViewModel

@Composable
fun AppNavigator(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = if (authViewModel.isAuthenticated()) "transactions" else "login"
    ) {
        composable("login") {
            LoginScreen(authViewModel = authViewModel) {
                navController.navigate("transactions") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable("transactions") {
            SplashScreen2()  // Aquí se muestra la lista de transacciones
        }
    }
}
