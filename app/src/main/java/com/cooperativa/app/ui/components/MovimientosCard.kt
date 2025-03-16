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
    val signoMonto = if (transaccion.tipo == TipoTransaccion.RETIRO) "-" else "+"
    val colorMonto = if (transaccion.tipo == TipoTransaccion.RETIRO) Color(0xFFFF7262) else Color(0xFF3EC27E)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)  // Se integra con el Card padre
            .padding(16.dp),         // Espacio interno
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Parte izquierda: ícono + descripción
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF2F4F7), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.BusinessCenter,
                    contentDescription = null,
                    tint = Color(0xFF081B61),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = transaccion.tipo.name,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transaccion.sucursal,
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        // Parte derecha: monto + fecha
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$signoMonto ${transaccion.monto} Bs.",
                color = colorMonto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = transaccion.fecha,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

// 2) Card único que contiene hasta 5 transacciones unidas
@Composable
fun UltimosMovimientosCard(transacciones: List<Transaccion>) {
    // Tomamos solo 5 (o menos, si hay menos de 5)
    val transaccionesLimitadas = transacciones.take(5)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // Márgenes laterales del bloque
    ) {
        Column {
            transaccionesLimitadas.forEachIndexed { index, transaccion ->
                // Divider opcional para separar ítems
                if (index != 0) {
                    Divider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp
                    )
                }
                // Cada ítem se dibuja sin Card, para lucir unidos
                TransaccionItemNoCard(transaccion)
            }
        }
    }
}