package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Payment
import com.washingapp.washing_backend.repository.BookingRepository
import com.washingapp.washing_backend.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val bookingRepository: BookingRepository
) {

    @Transactional
    fun pay(bookingId: UUID): Payment {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (booking.status != "PENDING") {
            throw RuntimeException("Only pending bookings can be paid")
        }

        if (paymentRepository.existsByBookingId(bookingId)) {
            throw RuntimeException("Payment already exists")
        }

        val amount = booking.washType.price

        val payment = Payment(
            booking = booking,
            providerPaymentId = "SIMULATED_${UUID.randomUUID()}",
            status = "SUCCESS",
            amount = amount
        )

        booking.status = "PAID"

        bookingRepository.save(booking)

        return paymentRepository.save(payment)
    }
}