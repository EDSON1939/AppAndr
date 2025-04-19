package com.cooperativa.app.ui.screens.components
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cooperativa.app.data.models.Cuenta
import com.google.accompanist.pager.*
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountCarousel(cuentas: List<Cuenta>) {
    if (cuentas.isEmpty()) return

    val pagerState = rememberPagerState(initialPage = 0)

    Column(modifier = Modifier.fillMaxWidth()) {
        // Ajusta contentPadding e itemSpacing para que no se recorte el indicador
        HorizontalPager(
            count = cuentas.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 8.dp),
            itemSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            // Aumentamos un poco el ancho si deseas que se vea más grande
            // pero cuidado con recortes en el indicador
            FancyCuentaCard(
                cuenta = cuentas[page],
                modifier = Modifier.width(300.dp)
            )
        }

        // Indicador de páginas (puntos)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(cuentas.size) { index ->
                val isSelected = (index == pagerState.currentPage)
                val size = if (isSelected) 16.dp else 8.dp
                val color = if (isSelected) Color(0xFF081B61) else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .size(size)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}
*/