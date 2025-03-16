package com.cooperativa.app.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cooperativa.app.data.models.Cuenta
import com.cooperativa.app.data.models.TipoCuenta
import com.cooperativa.app.navigation.AppNavigator
import com.cooperativa.app.ui.components.BottomNavigationBar
import com.cooperativa.app.ui.components.TransaccionItem
import com.cooperativa.app.viewmodel.CuentasViewModel
import com.cooperativa.app.ui.components.CuentaCard
import com.cooperativa.app.ui.components.CuentaPrincipalCard
import com.cooperativa.app.ui.components.UltimosMovimientosCard
import com.cooperativa.app.viewmodel.AuthViewModel


@Composable
fun MainScreen(tipoCuenta: TipoCuenta, viewModel: CuentasViewModel = viewModel()) {
    val cuentas = viewModel.obtenerCuentasPorTipo(tipoCuenta)
    val cuentaPrincipal = cuentas.firstOrNull()

    // Estructura general
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // si quieres scroll en todo
    ) {
        // Carrusel de cuentas (opcional)
        if (cuentas.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 16.dp)
            ) {
                cuentas.forEach { cuenta ->
                    CuentaCard(
                        cuenta = cuenta,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        // Sección "Últimos movimientos"
        if (cuentaPrincipal != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Últimos movimientos",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Ver todo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                    // Acciones para navegar a transacciones completas
                )
            }

            // Muestra un solo bloque (Card) con hasta 5 transacciones
            UltimosMovimientosCard(cuentaPrincipal.transacciones)
        }
    }
}