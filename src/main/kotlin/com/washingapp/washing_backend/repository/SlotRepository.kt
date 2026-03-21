package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Slot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface SlotRepository : JpaRepository<Slot, Long> {

    fun findTopByMachineIdOrderByStartTimeDesc(machineId: Long): Slot?

    @Query("""
        SELECT s FROM Slot s
        WHERE s.machine.id = :machineId
        AND NOT EXISTS (
            SELECT b FROM Booking b
            WHERE b.startSlot.machine.id = :machineId
            AND b.status <> 'CANCELLED'
            AND b.startTime < s.endTime
            AND b.endTime > s.startTime
        )
    """)
    fun findAvailableSlots(@Param("machineId") machineId: Long): List<Slot>
}