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
import com.cooperativa.app.data.models.Cuenta
import com.cooperativa.app.data.models.TipoTransaccion
import com.cooperativa.app.data.models.Transaccion

@Composable
fun TransaccionItem(transaccion: Transaccion) {
    // En lugar de un Card, usa un Row o Box con fondo blanco (si quieres un fondo).
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White) // O tu color preferido
            .padding(16.dp),         // Ajusta si deseas algo de espacio interno
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Parte izquierda
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
        // Parte derecha
        val signoMonto = if (transaccion.tipo == TipoTransaccion.RETIRO) "-" else "+"
        val colorMonto = if (transaccion.tipo == TipoTransaccion.RETIRO) Color(0xFFFF7262) else Color(0xFF3EC27E)
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
