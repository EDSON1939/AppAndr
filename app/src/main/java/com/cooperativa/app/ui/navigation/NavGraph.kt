package com.cooperativa.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cooperativa.app.ui.screens.accounts.AccountAndMovements
import com.cooperativa.app.ui.screens.auth.CreatePasswordScreen
import com.cooperativa.app.ui.screens.auth.LoginScreen
import com.cooperativa.app.ui.viewmodel.AuthViewModel
import com.cooperativa.app.ui.viewmodel.AuthViewModelFactory


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModelFactory: AuthViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGIN
    ) {
        // Pantalla de Login
        composable(Destinations.LOGIN) {
            LoginScreen(
                authViewModelFactory = authViewModelFactory,
                onLoginSuccess = { isFirstLogin ->
                    if (isFirstLogin) {
                        // Navegar a creación de contraseña con limpieza de backstack
                        navController.navigate(Destinations.CREATE_PASSWORD) {
                            popUpTo(Destinations.LOGIN) { inclusive = true }
                        }
                    } else {
                        // Ir directamente al main con limpieza de backstack
                        navController.navigate(Destinations.ACCOUNTSMOVEMENTS) {
                            popUpTo(Destinations.LOGIN) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Pantalla de Creación de Contraseña
        composable(Destinations.CREATE_PASSWORD) {
            CreatePasswordScreen(
                authViewModelFactory = authViewModelFactory,
                onPasswordChanged = {
                    // Después de cambiar contraseña, ir a MAIN limpiando el backstack
                    navController.navigate(Destinations.ACCOUNTSMOVEMENTS) {
                        popUpTo(0) // Limpia toda la pila de navegación
                    }
                }
            )
        }

        // Pantalla Principal
        composable(Destinations.ACCOUNTSMOVEMENTS) {
            // Aquí iría tu MainScreen o HomeScreen
            AccountAndMovements(
                onLogout = {
                    // Al hacer logout, volvemos a LOGIN limpiando todo
                    navController.navigate(Destinations.LOGIN) {
                        popUpTo(0)
                    }
                }
            )
        }


    }
}
