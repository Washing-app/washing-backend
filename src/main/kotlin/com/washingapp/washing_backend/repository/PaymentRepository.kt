package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentRepository : JpaRepository<Payment, Long> {

    fun existsByBookingId(bookingId: UUID): Boolean

    fun findByBookingId(bookingId: UUID): Payment?
}