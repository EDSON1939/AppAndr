package com.cooperativa.app.ui.screens.components

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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController



@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    tokenExpired: Boolean
) {
    // Obtenemos la altura de la pantalla en dp.
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.toFloat()

    // Calculamos la altura como el 10% de la altura de la pantalla.
    // Luego forzamos que esté entre 56.dp y 85.dp.
    val computedHeight = (screenHeightDp * 0.10f).dp
    val navBarHeight = when {
        computedHeight < 56.dp -> 56.dp
        computedHeight > 85.dp -> 85.dp
        else -> computedHeight
    }

    NavigationBar(
        modifier = Modifier.height(navBarHeight),
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        // Ítem "Ahorros"
        NavigationBarItem(
            selected = currentRoute == "ahorros",
            onClick = {
                if (tokenExpired) {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute != "ahorros") {
                    navController.navigate("ahorros")
                }
            },
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
                        popUpTo("login") { inclusive = true }
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
                        popUpTo("login") { inclusive = true }
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