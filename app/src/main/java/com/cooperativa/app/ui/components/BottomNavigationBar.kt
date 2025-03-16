package com.cooperativa.app.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    tokenExpired: Boolean
) {
    NavigationBar(
        modifier = Modifier.height(85.dp),
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        // Ítem "Ahorros"
        NavigationBarItem(
            selected = currentRoute == "ahorros",
            onClick = {
                if (tokenExpired) {
                    navController.navigate("login") {
                        popUpTo("ahorros") { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute != "ahorros") {
                    navController.navigate("ahorros")
                }
            },
            // No deshabilitamos el botón, permitiendo que se pulse siempre
            icon = {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = "Ahorros",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(text = "AHORROS", fontSize = 12.sp)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF081B61),
                selectedTextColor = Color(0xFF081B61),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        // Ítem "Créditos"
        NavigationBarItem(
            selected = currentRoute == "creditos",
            onClick = {
                if (tokenExpired) {
                    navController.navigate("login") {
                        popUpTo("creditos") { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute != "creditos") {
                    navController.navigate("creditos")
                }
            },
            icon = {
                Icon(
                    Icons.Default.CreditCard,
                    contentDescription = "Créditos",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(text = "CRÉDITOS", fontSize = 12.sp)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF081B61),
                selectedTextColor = Color(0xFF081B61),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )

        // Ítem "Aportes"
        NavigationBarItem(
            selected = currentRoute == "aportes",
            onClick = {
                if (tokenExpired) {
                    navController.navigate("login") {
                        popUpTo("aportes") { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute != "aportes") {
                    navController.navigate("aportes")
                }
            },
            icon = {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = "Aportes",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = {
                Text(text = "APORTES", fontSize = 12.sp)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF081B61),
                selectedTextColor = Color(0xFF081B61),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
