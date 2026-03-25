package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Slot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import java.time.LocalDate
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
    fun existsByMachineIdAndStartTime(machineId: Long, startTime: LocalDateTime): Boolean

    @Query("""
    select s
    from Slot s
    where s.machine.id = :machineId
      and s.startTime >= :startOfDay
      and s.startTime < :endOfDay
      and s.isBooked = false
    order by s.startTime
""")
    fun findAvailableSlotsByMachineAndDateRange(
        @Param("machineId") machineId: Long,
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime
    ): List<Slot>

    @Query("""
    select distinct s.machine.id
    from Slot s
    where s.startTime >= :startOfDay
      and s.startTime < :endOfDay
      and s.isBooked = false
""")
    fun findMachineIdsWithAvailableSlotsInRange(
        @Param("startOfDay") startOfDay: LocalDateTime,
        @Param("endOfDay") endOfDay: LocalDateTime
    ): List<Long>

    @Query("""
    select s
    from Slot s
    where s.machine.id = :machineId
      and s.startTime < :endTime
      and s.endTime > :startTime
    order by s.startTime
""")
    fun findAllInInterval(
        machineId: Long,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): List<Slot>
}