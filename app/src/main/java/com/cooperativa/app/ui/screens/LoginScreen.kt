package com.cooperativa.app.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.viewmodel.AuthViewModel
import com.cooperativa.app.viewmodel.LoginState


@Composable
fun LoginScreen(authViewModel: AuthViewModel = viewModel(), onLoginSuccess: () -> Unit = {}) {
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(uiState.token) {
        if (uiState.token != null) {
            onLoginSuccess()
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordIncorrect by remember { mutableStateOf(false) }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.15f)) // Espaciado dinámico arriba

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Título
                Text(
                    text = "Iniciar Sesión",
                    fontSize = if (screenWidth < 400.dp) 22.sp else 26.sp, // Tamaño dinámico
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo de usuario
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { authViewModel.onUsernameChanged(it) }, // ✅ Usa el ViewModel
                    label = { Text("Usuario") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Blue,
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f) // 90% del ancho
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de contraseña con validación
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { authViewModel.onPasswordChanged(it) }, // ✅ Usa el ViewModel
                    label = { Text("Contraseña") },
                    singleLine = true,
                    isError = isPasswordIncorrect, // Se marca en rojo si es incorrecta
                    shape = RoundedCornerShape(12.dp), // Bordes redondeados
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isPasswordIncorrect) Color.Red else Color.Blue,
                        unfocusedBorderColor = if (isPasswordIncorrect) Color.Red else Color.Gray,
                        cursorColor = Color.Blue,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedLabelColor = if (isPasswordIncorrect) Color.Red else Color.Blue,
                        unfocusedLabelColor = if (isPasswordIncorrect) Color.Red else Color.Gray
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                // Mensaje de error si la contraseña es incorrecta
                if (uiState.isPasswordIncorrect) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Contraseña incorrecta. Verifica tu contraseña nuevamente.",
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp)) // Más separación del botón

                // Botón de Iniciar Sesión (Deshabilitado si no hay contraseña)
                Button(
                    onClick = {
                        authViewModel.login(onLoginSuccess) // ✅ Pasamos el callback correctamente
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp), // Bordes redondeados en el botón
                    colors = ButtonDefaults.buttonColors(containerColor = if (uiState.password.isNotEmpty()) Color.Blue else Color.Gray),
                    enabled = uiState.password.isNotEmpty()
                ) {
                    Text(text = "Iniciar Sesión", fontSize = 18.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.1f)) // Más espacio en pantallas grandes

            // Nombre "CoopApp" en la parte inferior
            Text(
                text = "CoopApp",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp) // Espacio inferior
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
