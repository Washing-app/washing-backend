package com.washingapp.washing_backend.dto

import java.math.BigDecimal

data class PrepareBookingResponse(
    val paymentRequired: Boolean,
    val blockedByUnpaidBooking: Boolean,
    val existingUnpaidBookingId: String?,
    val slotId: Long,
    val washTypeId: Long,
    val price: BigDecimal
)
