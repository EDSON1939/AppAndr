package com.cooperativa.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cooperativa.app.ui.theme.PrimaryBlue
import com.cooperativa.app.ui.theme.TextSecondary


@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Ahorros",
                    tint = if (selectedTab == 0) PrimaryBlue else TextSecondary
                )
            },
            label = {
                Text(
                    "Ahorros",
                    color = if (selectedTab == 0) PrimaryBlue else TextSecondary
                )
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = "Créditos",
                    tint = if (selectedTab == 1) PrimaryBlue else TextSecondary
                )
            },
            label = {
                Text(
                    "Créditos",
                    color = if (selectedTab == 1) PrimaryBlue else TextSecondary
                )
            },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = "Certificados",
                    tint = if (selectedTab == 2) PrimaryBlue else TextSecondary
                )
            },
            label = {
                Text(
                    "Certificados",
                    color = if (selectedTab == 2) PrimaryBlue else TextSecondary
                )
            },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}