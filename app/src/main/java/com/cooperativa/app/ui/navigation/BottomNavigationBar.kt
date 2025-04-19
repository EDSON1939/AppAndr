package com.cooperativa.app.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.cooperativa.app.R

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground), // Cambia por tu icono
                    contentDescription = "Ahorros"
                )
            },
            label = { Text("Ahorros") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_background), // Cambia por tu icono
                    contentDescription = "Créditos"
                )
            },
            label = { Text("Créditos") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground), // Cambia por tu icono
                    contentDescription = "Aportes"
                )
            },
            label = { Text("Aportes") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) }
        )
    }
}