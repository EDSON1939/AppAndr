package com.cooperativa.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cooperativa.app.ui.screens.accounts.AccountAndMovements
import com.cooperativa.app.ui.screens.accounts.AccountDetailRoute
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountDetailViewModelFactory
import com.cooperativa.app.ui.screens.accounts.viewmodel.AccountsViewModelFactory
import com.cooperativa.app.ui.screens.auth.CreatePasswordScreen
import com.cooperativa.app.ui.screens.auth.LoginScreen
import com.cooperativa.app.ui.viewmodel.AuthViewModel
import com.cooperativa.app.ui.viewmodel.AuthViewModelFactory


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModelFactory: AuthViewModelFactory,
    accountsViewModelFactory: AccountsViewModelFactory,
    accountDetailViewModelFactory: AccountDetailViewModelFactory
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

        composable(Destinations.ACCOUNTSMOVEMENTS) {
            AccountAndMovements(
                accountsViewModelFactory = accountsViewModelFactory,
                onLogout = {
                    navController.navigate(Destinations.LOGIN) { popUpTo(0) }
                },
                navController = navController
            )
        }
/*
        composable(
            route = "movements/{accountId}",
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getString("accountId") ?: ""
            AllMovementsScreen(
                accountId = accountId,
                onBackClick = { navController.popBackStack() },
                viewModel = viewModel(factory = accountsViewModelFactory)
            )
        }
*/
        composable(
            route = "accountDetail/{tipo}/{cuenta}",
            arguments = listOf(
                navArgument("tipo")   { type = NavType.IntType },
                navArgument("cuenta") { type = NavType.StringType }
            )
        ) { backStack ->
            val tipo   = backStack.arguments!!.getInt("tipo")
            val cuenta = backStack.arguments!!.getString("cuenta")!!
            AccountDetailRoute(
                navController,
                tipo,
                cuenta,
                accountDetailViewModelFactory   // <-- instancia, no clase
            )
        }

    }
}
