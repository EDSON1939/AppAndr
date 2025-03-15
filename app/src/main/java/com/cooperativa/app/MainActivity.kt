package com.cooperativa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import com.cooperativa.app.ui.screens.LoginScreen // Asegúrate de importar LoginScreen

import android.util.Log
import androidx.compose.runtime.Composable

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color


import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.data.remote.AuthRepository
import com.cooperativa.app.data.remote.RetrofitInstance
import com.cooperativa.app.ui.screens.LoginScreen
import com.cooperativa.app.ui.screens.SplashScreen
import com.cooperativa.app.viewmodel.AuthViewModel
import com.cooperativa.app.viewmodel.AuthViewModelFactory
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

//import androidx.compose.runtime.rememberSaveable

import com.cooperativa.app.ui.screens.SplashScreen2 // 🚀 Nueva pantalla


import androidx.compose.runtime.*
import androidx.navigation.NavHostController

import com.cooperativa.app.data.local.TokenManager
import com.cooperativa.app.navigation.AppNavigator
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(applicationContext)


        setContent {
            val navController = rememberNavController()
            val authRepository = remember { AuthRepository(RetrofitInstance.api) }
            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository, tokenManager))

            // 📌 Llamamos `AppNavigator`
            AppNavigator(navController, authViewModel, tokenManager)
        }
    }
}



/*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivityskip", "onCreate iniciado")

        setContent {
            Log.d("MainActivityskip", "setContent ejecutado")

            var showSplash by rememberSaveable { mutableStateOf(true) }

            // ✅ Solo creamos el ViewModel UNA VEZ
            val authRepository = remember { AuthRepository(RetrofitInstance.api) }
            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))

            if (showSplash) {
                SplashScreen {
                    Log.d("MainActivityskip", "Splash terminado, yendo a LoginScreen")
                    showSplash = false
                }
            } else {
                Log.d("MainActivityskip", "AuthViewModel creado correctamente")
                LoginScreen(authViewModel)
            }
        }
    }
}*/

/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivityskip", "onCreate iniciado")

        setContent {
            Log.d("MainActivityskip", "setContent ejecutado")


            var showSplash by rememberSaveable { mutableStateOf(true) }

            //var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {
                SplashScreen {
                    Log.d("MainActivityskip", "Splash terminado, mostrando LoginScreen")
                    showSplash = false }
            } else {
                // ✅ Solo creamos `AuthViewModel` dentro de `setContent`
               // val authRepository = AuthRepository(RetrofitInstance.api)
                //val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))

                val authRepository = remember { AuthRepository(RetrofitInstance.api) }
                val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(authRepository))


                Log.d("MainActivityskip", "AuthViewModel creado")

                LoginScreen(authViewModel)
            }
        }
    }
}*/
