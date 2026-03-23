package com.washingapp.washing_backend.dto

import java.math.BigDecimal

data class CreateWashTypeRequest(
    val name: String,
    val durationMinutes: Int,
    val price: BigDecimal,
    val temperature: Int,
    val spinSpeed: Int,
    val description: String,
    val id: Long,
    val imageLink: String?
)