package com.washingapp.washing_backend.dto

data class UserCreateRequest(
    val name: String,
    val phone: String,
    val password: String
)