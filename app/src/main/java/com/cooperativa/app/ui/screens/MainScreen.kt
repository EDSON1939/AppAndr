package com.cooperativa.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cooperativa.app.data.models.TipoCuenta
import com.cooperativa.app.ui.viewmodel.CuentasViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.ui.screens.components.AccountCarousel
import com.cooperativa.app.ui.screens.components.UltimosMovimientosCard

@Composable
fun MainScreen(
    tipoCuenta: TipoCuenta,
    viewModel: CuentasViewModel = viewModel()
) {
    val cuentas = viewModel.obtenerCuentasPorTipo(tipoCuenta)
    val cuentaPrincipal = cuentas.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // scroll en toda la pantalla
    ) {
        // Parte superior: fondo blanco + carrusel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            if (cuentas.isNotEmpty()) {
                // Coloca un Spacer si quieres separar un poco
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    AccountCarousel(cuentas = cuentas)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        // Parte inferior: fondo gris (del theme) + "Últimos movimientos"
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
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
                        )
                    }
                    // Card con los 5 movimientos
                    UltimosMovimientosCard(cuentaPrincipal.transacciones)
                }
            }
        }
    }
}
