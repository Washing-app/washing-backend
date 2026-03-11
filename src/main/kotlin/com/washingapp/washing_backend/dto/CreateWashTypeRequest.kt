package com.washingapp.washing_backend.dto

import java.math.BigDecimal

data class CreateWashTypeRequest(
    val name: String,
    val durationMinutes: Int,
    val price: BigDecimal
)