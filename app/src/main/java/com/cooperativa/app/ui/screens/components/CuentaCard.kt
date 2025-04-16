package com.cooperativa.app.ui.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooperativa.app.data.models.Cuenta

@Composable
fun FancyCuentaCard(cuenta: Cuenta, modifier: Modifier = Modifier) {
    // Color base y color de la onda
    val baseColor = Color(0xFF081B61)
    val waveColor = baseColor.copy(alpha = 0.9f)

    Card(
        modifier = modifier
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = baseColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Onda de fondo
            Canvas(modifier = Modifier.matchParentSize()) {
                val width = size.width
                val height = size.height

                val path = Path().apply {
                    moveTo(width, 0f)
                    // Ajusta estos puntos para que la curva se parezca más a tu mockup
                    cubicTo(
                        width * 0.6f, 0f,
                        width * 0.4f, height * 0.4f,
                        0f, height
                    )
                    lineTo(width, height)
                    close()
                }
                drawPath(path = path, color = waveColor)
            }

            // Contenido de texto
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Fila superior: tipo de cuenta y saldo a la derecha
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Columna con "AHORRO" y "CUENTA DE AHORRO"
                    Column {
                        Text(
                            text = cuenta.nombreCuenta, // Ej: "AHORRO"
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "CUENTA DE ${cuenta.nombreCuenta}",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                    // Texto del saldo arriba a la derecha
                    Text(
                        text = "SALDO ${cuenta.saldo} Bs.",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Fila inferior (centrar el número de cuenta)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "NR ${cuenta.numeroCuenta}", // Ej: "NR 112000730"
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}