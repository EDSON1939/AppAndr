package com.cooperativa.app.ui.screens.auth
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.text.KeyboardOptions
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cooperativa.app.ui.viewmodel.AuthViewModel
import com.cooperativa.app.ui.viewmodel.AuthViewModelFactory


@Composable
fun CreatePasswordScreen(
    authViewModelFactory: AuthViewModelFactory,
    viewModel: AuthViewModel = viewModel(factory = authViewModelFactory),
    onPasswordChanged: () -> Unit = {}
) {
    val uiState by viewModel.passwordCreationState.collectAsState()
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onPasswordChanged()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(screenHeight * 0.1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Creación de Clave",
                    fontSize = if (screenWidth < 400.dp) 22.sp else 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Current Password (non-editable)
                OutlinedTextField(
                    value = uiState.currentPassword,
                    onValueChange = { viewModel.onCurrentPasswordChanged(it) },
                    label = { Text("CLAVE ACTUAL") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.Gray,
                        disabledTextColor = MaterialTheme.colorScheme.onBackground,
                        disabledLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // New Password
                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = { viewModel.onNewPasswordChanged(it) },
                    label = { Text("NUEVA CLAVE") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Ocultar contraseña"
                                else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Repeat Password
                OutlinedTextField(
                    value = uiState.repeatPassword,
                    onValueChange = { viewModel.onRepeatPasswordChanged(it) },
                    label = { Text("REPITA CLAVE") },
                    singleLine = true,
                    isError = uiState.newPassword != uiState.repeatPassword,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (uiState.newPassword != uiState.repeatPassword) Color.Red
                        else MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (uiState.newPassword != uiState.repeatPassword) Color.Red
                        else Color.Gray,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedLabelColor = if (uiState.newPassword != uiState.repeatPassword) Color.Red
                        else MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = if (uiState.newPassword != uiState.repeatPassword) Color.Red
                        else Color.Gray
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth(0.9f)
                )

                if (uiState.newPassword.isNotEmpty() &&
                    uiState.repeatPassword.isNotEmpty() &&
                    uiState.newPassword != uiState.repeatPassword) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Error de contraseña",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Las Claves no son iguales, verifica otra vez.",
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (uiState.newPassword == uiState.repeatPassword) {
                            viewModel.changePassword(
                                currentPassword = uiState.currentPassword,
                                newPassword = uiState.newPassword,
                                onSuccess = onPasswordChanged
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.newPassword.isNotEmpty() &&
                            uiState.repeatPassword.isNotEmpty() &&
                            uiState.newPassword == uiState.repeatPassword)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Gray
                    ),
                    enabled = uiState.newPassword.isNotEmpty() &&
                            uiState.repeatPassword.isNotEmpty() &&
                            uiState.newPassword == uiState.repeatPassword &&
                            !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        Text(text = "Cambiar Clave", fontSize = 18.sp, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.1f))

            Text(
                text = "COOPERATIVA APP",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}