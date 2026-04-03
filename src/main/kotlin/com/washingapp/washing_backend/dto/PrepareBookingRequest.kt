package com.washingapp.washing_backend.dto

data class PrepareBookingRequest(
    val slotId: Long,
    val washTypeId: Long
)
