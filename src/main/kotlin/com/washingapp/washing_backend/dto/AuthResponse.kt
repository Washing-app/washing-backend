package com.washingapp.washing_backend.dto

data class AuthResponse(
    val token: String,
    val userId: String
)
