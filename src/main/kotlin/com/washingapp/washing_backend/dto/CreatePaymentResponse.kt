package com.washingapp.washing_backend.dto

data class CreatePaymentResponse(
    val bookingId: String,
    val paymentId: String,
    val status: String,
    val confirmationUrl: String
)