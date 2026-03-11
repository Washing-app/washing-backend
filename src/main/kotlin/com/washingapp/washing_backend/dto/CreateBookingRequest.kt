package com.washingapp.washing_backend.dto

import java.util.UUID

data class CreateBookingRequest(
    val userId: UUID,
    val slotId: Long,
    val washTypeId: Long
)