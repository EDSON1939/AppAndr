package com.cooperativa.app.ui.screens.accounts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountAndMovements(onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pantalla Principal") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout")
                    }
                }
            )
        }
    ) { padding ->
        // Contenido principal
        Box(modifier = Modifier.padding(padding)) {
            Text("Bienvenido a la app")
        }
    }
}