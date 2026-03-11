package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.repository.*
import org.springframework.stereotype.Service
import java.util.UUID

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
}