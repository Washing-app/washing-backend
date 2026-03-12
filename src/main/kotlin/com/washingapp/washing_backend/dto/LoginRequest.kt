package com.washingapp.washing_backend.dto

data class LoginRequest(
    val phone: String,
    val password: String
)