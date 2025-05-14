package com.cooperativa.app.ui.screens.accounts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooperativa.app.data.models.Account
import com.cooperativa.app.data.models.Movement


@Composable
fun MovementItem(movement: Movement) {
    val signo = if (movement.amount > 0) "+" else "-"
    val colorMonto = if (movement.amount > 0) Color(0xFF3EC27E) else Color(0xFFFF7262)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = movement.description,
                fontWeight = FontWeight.Bold
            )
            movement.location?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "$signo ${movement.amount} movement.currency",
                color = colorMonto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = movement.date,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}