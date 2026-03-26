package com.washingapp.washing_backend.dto

data class BookingResponse(
    val id: String,
    val status: String,
    val startTime: String,
    val endTime: String,
    val cancelledAt: String?,
    val machineName: String,
    val washType: WashTypeShortResponse
)

data class WashTypeShortResponse(
    val id: Long,
    val name: String,
    val durationMinutes: Int,
    val price: Int,
    val temperature: Int,
    val spinSpeed: Int
)
