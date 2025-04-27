package com.cooperativa.app.data.models

data class LoginRequest(
    val usuario: String,
    val password: String,
    val deviceInfo: Map<String, String>

)
