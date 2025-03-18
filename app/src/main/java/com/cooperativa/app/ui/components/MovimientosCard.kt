package com.cooperativa.app.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooperativa.app.data.models.Transaccion
import com.cooperativa.app.data.models.TipoTransaccion

// 1) Ítem sin Card (solo un Row con fondo blanco)
@Composable
fun TransaccionItemNoCard(transaccion: Transaccion) {
    val signo = if (transaccion.tipo == TipoTransaccion.RETIRO) "-" else "+"
    val colorMonto = if (transaccion.tipo == TipoTransaccion.RETIRO) Color(0xFFFF7262) else Color(0xFF3EC27E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = transaccion.tipo.name,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = transaccion.sucursal,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$signo ${transaccion.monto} Bs.",
                color = colorMonto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = transaccion.fecha,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}


@Composable
fun UltimosMovimientosCard(transacciones: List<Transaccion>) {
    val transaccionesLimitadas = transacciones.take(5)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column {
            transaccionesLimitadas.forEachIndexed { index, transaccion ->
                if (index != 0) {
                    Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                }
                TransaccionItemNoCard(transaccion)
            }
        }
    }
}
