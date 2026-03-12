package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, Long> {

    fun existsByBookingId(bookingId: java.util.UUID): Boolean
}