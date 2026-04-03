package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.UUID

interface BookingRepository : JpaRepository<Booking, UUID> {


    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.startSlot.machine.id = :machineId
    AND b.status <> 'CANCELLED'
    AND (
        b.startTime < :endTime
        AND b.endTime > :startTime
    )
    """)
    fun existsOverlappingBooking(
        machineId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Boolean

    @Query("""
    select b
    from Booking b
    where b.startSlot.machine.id = :machineId
      and b.startTime < :endOfDay
      and b.endTime > :startOfDay
      and b.status <> 'CANCELLED'
    order by b.startTime
""")
    fun findAllByMachineAndDay(
        machineId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<Booking>

    fun findAllByUserIdOrderByStartTimeAsc(userId: UUID): List<Booking>

    fun existsByUserId(userId: UUID): Boolean

    fun findFirstByUserIdAndStatusAndEndTimeAfterOrderByCreatedAtAsc(
        userId: UUID,
        status: String,
        endTime: LocalDateTime
    ): Booking?
}