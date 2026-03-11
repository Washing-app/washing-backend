package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.repository.*
import org.springframework.stereotype.Service
import java.util.UUID
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val slotRepository: SlotRepository,
    private val userRepository: UserRepository,
    private val washTypeRepository: WashTypeRepository
) {

    fun create(requestUserId: UUID, slotId: Long, washTypeId: Long): Booking {

        if (bookingRepository.existsBySlotId(slotId)) {
            throw RuntimeException("Slot already booked")
        }

        val user = userRepository.findById(requestUserId)
            .orElseThrow { RuntimeException("User not found") }

        val slot = slotRepository.findById(slotId)
            .orElseThrow { RuntimeException("Slot not found") }

        val washType = washTypeRepository.findById(washTypeId)
            .orElseThrow { RuntimeException("WashType not found") }

        val booking = Booking(
            user = user,
            slot = slot,
            washType = washType,
            status = "PENDING"
        )

        return bookingRepository.save(booking)
    }

    @Transactional
    fun cancel(bookingId: UUID): Booking {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (booking.status == "CANCELLED") {
            throw RuntimeException("Booking already cancelled")
        }

        if (booking.status == "COMPLETED") {
            throw RuntimeException("Cannot cancel completed booking")
        }

        val updatedBooking = booking.copy(
            status = "CANCELLED",
            cancelledAt = LocalDateTime.now()
        )

        return bookingRepository.save(updatedBooking)
    }

    @Transactional
    fun complete(bookingId: UUID): Booking {

        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (booking.status == "CANCELLED") {
            throw RuntimeException("Cannot complete cancelled booking")
        }

        if (booking.status == "COMPLETED") {
            throw RuntimeException("Booking already completed")
        }

        if (booking.status != "PAID") {
            throw RuntimeException("Only paid bookings can be completed")
        }

        val updatedBooking = booking.copy(
            status = "COMPLETED"
        )

        return bookingRepository.save(updatedBooking)
    }
}