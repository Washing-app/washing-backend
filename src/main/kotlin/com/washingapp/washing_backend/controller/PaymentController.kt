package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.entity.Payment
import com.washingapp.washing_backend.service.PaymentService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping("/{bookingId}")
    fun pay(@PathVariable bookingId: UUID): Payment {
        return paymentService.pay(bookingId)
    }
}