package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.CreateBookingRequest
import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.service.BookingService
import org.springframework.web.bind.annotation.*

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
}