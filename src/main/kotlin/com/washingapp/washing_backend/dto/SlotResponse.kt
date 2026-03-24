package com.washingapp.washing_backend.dto

data class SlotResponse(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val isBooked: Boolean
)
