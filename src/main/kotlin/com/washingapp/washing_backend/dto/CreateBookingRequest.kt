package com.washingapp.washing_backend.dto

data class CreateBookingRequest(
    val slotId: Long,
    val washTypeId: Long
)