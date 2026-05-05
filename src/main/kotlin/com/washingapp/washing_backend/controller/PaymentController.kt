package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.CreatePaymentResponse
import com.washingapp.washing_backend.security.JwtService
import com.washingapp.washing_backend.service.PaymentService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService,
    private val jwtService: JwtService
) {

    @PostMapping("/bookings/{bookingId}/checkout")
    fun createPayment(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable bookingId: UUID
    ): CreatePaymentResponse {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.extractUserId(token)

        return paymentService.createPaymentForBooking(
            userId = userId,
            bookingId = bookingId
        )
    }
}