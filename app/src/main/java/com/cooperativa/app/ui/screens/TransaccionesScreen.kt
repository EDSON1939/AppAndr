package com.cooperativa.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.ui.viewmodel.TransaccionesViewModel
import com.cooperativa.app.ui.screens.components.TransaccionItem

@Composable
fun TransaccionesScreen(viewModel: TransaccionesViewModel = viewModel()) {
    val transacciones = viewModel.transacciones
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Todas las transacciones",
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn {
            items(transacciones) { transaccion ->
                TransaccionItem(transaccion)
            }
        }
    }
}