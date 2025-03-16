package com.cooperativa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.data.local.TokenManager
import com.cooperativa.app.data.remote.AuthRepository
import com.cooperativa.app.data.remote.RetrofitInstance
import com.cooperativa.app.navigation.AppContent
import com.cooperativa.app.ui.theme.CooperativaAppTheme
import com.cooperativa.app.viewmodel.AuthViewModel
import com.cooperativa.app.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokenManager = TokenManager(applicationContext)
        setContent {
            CooperativaAppTheme(dynamicColor = false) {
                // Creamos un único NavController para toda la app.
                val navController = rememberNavController()
                val authRepository = AuthRepository(RetrofitInstance.api)
                val authViewModel: AuthViewModel = viewModel(
                    factory = AuthViewModelFactory(authRepository, tokenManager)
                )
                AppContent(navController, authViewModel)
            }
        }
    }
}