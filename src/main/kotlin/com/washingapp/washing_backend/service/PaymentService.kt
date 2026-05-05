package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.dto.CreatePaymentResponse
import com.washingapp.washing_backend.entity.Payment
import com.washingapp.washing_backend.repository.BookingRepository
import com.washingapp.washing_backend.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val bookingRepository: BookingRepository,
    private val yooKassaService: YooKassaService
) {

    @Transactional
    fun createPaymentForBooking(
        userId: UUID,
        bookingId: UUID
    ): CreatePaymentResponse {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (booking.user.id != userId) {
            throw RuntimeException("Access denied")
        }

        if (booking.status != "PENDING_PAYMENT") {
            throw RuntimeException("Payment can be created only for PENDING_PAYMENT booking")
        }

        val existingPayment = paymentRepository.findByBookingId(bookingId)
        if (existingPayment != null &&
            !existingPayment.confirmationUrl.isNullOrBlank() &&
            existingPayment.status == "pending"
        ) {
            return CreatePaymentResponse(
                bookingId = booking.id.toString(),
                paymentId = existingPayment.providerPaymentId ?: "",
                status = existingPayment.status,
                confirmationUrl = existingPayment.confirmationUrl!!
            )
        }

        val result = yooKassaService.createPayment(
            amount = booking.washType.price,
            description = "Оплата бронирования ${booking.id}",
            bookingId = booking.id
        )

        val saved = paymentRepository.save(
            existingPayment?.apply {
                providerPaymentId = result.paymentId
                status = result.status
                confirmationUrl = result.confirmationUrl
                idempotenceKey = result.idempotenceKey
            } ?: Payment(
                booking = booking,
                providerPaymentId = result.paymentId,
                status = result.status,
                amount = booking.washType.price,
                confirmationUrl = result.confirmationUrl,
                idempotenceKey = result.idempotenceKey
            )
        )

        return CreatePaymentResponse(
            bookingId = booking.id.toString(),
            paymentId = saved.providerPaymentId ?: "",
            status = saved.status,
            confirmationUrl = saved.confirmationUrl ?: ""
        )
    }
}