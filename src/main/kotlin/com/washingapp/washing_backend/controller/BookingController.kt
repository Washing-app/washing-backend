package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.CreateBookingRequest
import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.service.BookingService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {

    @PostMapping
    fun create(@RequestBody request: CreateBookingRequest): Booking {
        return bookingService.create(
            request.userId,
            request.slotId,
            request.washTypeId
        )
    }

    @PatchMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): Booking {
        return bookingService.cancel(id)
    }

    @PatchMapping("/{id}/complete")
    fun complete(@PathVariable id: UUID): Booking {
        return bookingService.complete(id)
    }
}