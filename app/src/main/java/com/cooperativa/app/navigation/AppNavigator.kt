package com.cooperativa.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cooperativa.app.ui.screens.LoginScreen
import com.cooperativa.app.ui.screens.SplashScreen2
import com.cooperativa.app.viewmodel.AuthViewModel

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.cooperativa.app.data.models.TipoCuenta
import com.cooperativa.app.ui.screens.MainContainerScreen
import com.cooperativa.app.ui.screens.MainScreen
import com.cooperativa.app.ui.screens.TransaccionesScreen



@Composable
fun AppNavigator(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    // Usamos el estado del ViewModel para determinar la ruta inicial.
    val uiState = authViewModel.uiState.collectAsState().value
    val tokenExpired = authViewModel.tokenExpired.collectAsState().value

    val startDestination = if (uiState.token != null && !tokenExpired) "ahorros" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(authViewModel) {
                navController.navigate("ahorros") {
                    popUpTo("login") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable("ahorros") {
            MainScreen(tipoCuenta = TipoCuenta.AHORRO)
        }
        composable("creditos") {
            MainScreen(tipoCuenta = TipoCuenta.CREDITO)
        }
        composable("aportes") {
            MainScreen(tipoCuenta = TipoCuenta.APORTE)
        }
        composable("transacciones") {
            TransaccionesScreen()
        }
    }
}


/*
@Composable
fun AppNavigator(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(
        navController = navController,
        startDestination = if (authViewModel.uiState.collectAsState().value.token != null) "transactions" else "login"
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
            TransaccionesScreen()
        }
    }
}*/