package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.BookingResponse
import com.washingapp.washing_backend.dto.CreateBookingRequest
import com.washingapp.washing_backend.dto.PrepareBookingRequest
import com.washingapp.washing_backend.dto.PrepareBookingResponse
import com.washingapp.washing_backend.entity.Booking
import com.washingapp.washing_backend.security.JwtService
import com.washingapp.washing_backend.service.BookingService
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService,
    private val jwtService: JwtService
) {

    @PostMapping
    fun create(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: CreateBookingRequest
    ): Booking {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.extractUserId(token)

        return bookingService.create(
            userId = userId,
            slotId = request.slotId,
            washTypeId = request.washTypeId
        )
    }

    @GetMapping("/my")
    fun getMyBookings(
        @RequestHeader("Authorization") authHeader: String
    ): List<BookingResponse> {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.extractUserId(token)

        return bookingService.getMyBookings(userId)
    }

    @GetMapping("/{id}")
    fun getBookingById(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: UUID
    ): BookingResponse {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.extractUserId(token)

        return bookingService.getBookingById(userId, id)
    }

    @PatchMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): Booking {
        return bookingService.cancel(id)
    }

    @PatchMapping("/{id}/complete")
    fun complete(@PathVariable id: UUID): Booking {
        return bookingService.complete(id)
    }
    @PostMapping("/prepare")
    fun prepare(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody request: PrepareBookingRequest
    ): PrepareBookingResponse {
        val token = authHeader.removePrefix("Bearer ").trim()
        val userId = jwtService.extractUserId(token)

        return bookingService.prepare(
            userId = userId,
            slotId = request.slotId,
            washTypeId = request.washTypeId
        )
    }
}