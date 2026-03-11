package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Booking
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookingRepository : JpaRepository<Booking, UUID> {

    fun existsBySlotId(slotId: Long): Boolean
}