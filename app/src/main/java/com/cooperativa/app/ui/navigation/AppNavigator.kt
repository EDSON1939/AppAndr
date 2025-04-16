

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