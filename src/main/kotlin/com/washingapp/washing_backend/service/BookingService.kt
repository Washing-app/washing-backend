package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.dto.BookingResponse
import com.washingapp.washing_backend.dto.WashTypeShortResponse
import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.repository.*
import org.springframework.data.domain.PageRequest
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

    @Transactional
    fun create(userId: UUID, slotId: Long, washTypeId: Long): Booking {

        val user = userRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        val slot = slotRepository.findById(slotId)
            .orElseThrow { RuntimeException("Slot not found") }

        val washType = washTypeRepository.findById(washTypeId)
            .orElseThrow { RuntimeException("Wash type not found") }

        val startTime = slot.startTime

        val totalDuration = washType.durationMinutes + 20

        val endTime = startTime.plusMinutes(totalDuration.toLong())

        val machineId = slot.machine.id

        val overlap = bookingRepository.existsOverlappingBooking(
            machineId,
            startTime,
            endTime
        )

        if (overlap) {
            throw RuntimeException("Selected time interval already booked")
        }

        val affectedSlots = slotRepository.findAllInInterval(
            machineId = machineId,
            startTime = startTime,
            endTime = endTime
        )

        if (affectedSlots.isEmpty()) {
            throw RuntimeException("No slots found for selected interval")
        }

        if (affectedSlots.any { it.isBooked }) {
            throw RuntimeException("Some slots in selected interval are already booked")
        }

        val updatedSlots = affectedSlots.map { it.copy(isBooked = true) }
        slotRepository.saveAll(updatedSlots)

        val booking = Booking(
            user = user,
            startSlot = slot,
            washType = washType,
            status = "PENDING",
            startTime = startTime,
            endTime = endTime
        )

        return bookingRepository.save(booking)
    }

    fun getMyBookings(userId: UUID): List<BookingResponse> {
        return bookingRepository.findAllByUserIdOrderByStartTimeAsc(userId)
            .map { it.toResponse() }
    }

    fun getBookingById(userId: UUID, bookingId: UUID): BookingResponse {
        val booking = bookingRepository.findById(bookingId)
            .orElseThrow { RuntimeException("Booking not found") }

        if (booking.user.id != userId) {
            throw RuntimeException("Access denied")
        }

        return booking.toResponse()
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

        val machineId = booking.startSlot.machine.id

        val affectedSlots = slotRepository.findAllInInterval(
            machineId = machineId,
            startTime = booking.startTime,
            endTime = booking.endTime
        )

        val freedSlots = affectedSlots.map { it.copy(isBooked = false) }
        slotRepository.saveAll(freedSlots)

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

    private fun Booking.toResponse(): BookingResponse {
        return BookingResponse(
            id = this.id.toString(),
            status = this.status,
            startTime = this.startTime.toString(),
            endTime = this.endTime.toString(),
            cancelledAt = this.cancelledAt?.toString(),
            machineName = this.startSlot.machine.name,
            washType = WashTypeShortResponse(
                id = this.washType.id,
                name = this.washType.name,
                durationMinutes = this.washType.durationMinutes,
                price = this.washType.price.toInt(),
                temperature = this.washType.temperature,
                spinSpeed = this.washType.spinSpeed
            )
        )
    }
}