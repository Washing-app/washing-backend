package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.dto.MachineResponse
import com.washingapp.washing_backend.dto.SlotResponse
import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.repository.BookingRepository
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import com.washingapp.washing_backend.repository.WashTypeRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MachineService(
    private val machineRepository: MachineRepository,
    private val slotRepository: SlotRepository,
    private val washTypeRepository: WashTypeRepository,
    private val bookingRepository: BookingRepository
) {

    fun create(machine: Machine): Machine {
        return machineRepository.save(machine)
    }

    fun getAll(): List<Machine> {
        return machineRepository.findAll()
    }

    fun getAvailableMachines(date: LocalDate): List<MachineResponse> {
        val start = date.atStartOfDay()
        val end = date.plusDays(1).atStartOfDay()

        val machineIds = slotRepository.findMachineIdsWithAvailableSlotsInRange(start, end)
        if (machineIds.isEmpty()) return emptyList()

        return machineRepository.findAllByIdIn(machineIds)
            .sortedWith(compareBy({ it.location }, { it.name }))
            .map {
                MachineResponse(
                    id = it.id,
                    name = it.name,
                    status = it.status,
                    location = it.location
                )
            }
    }

    fun getAvailableSlots(
        machineId: Long,
        date: LocalDate,
        washTypeId: Long
    ): List<SlotResponse> {

        val washType = washTypeRepository.findById(washTypeId)
            .orElseThrow { RuntimeException("Wash type not found") }

        val totalDurationMinutes = washType.durationMinutes + 20

        val dayStart = date.atStartOfDay()
        val dayEnd = date.plusDays(1).atStartOfDay()

        val daySlots = slotRepository.findAllByMachineAndDay(
            machineId = machineId,
            startOfDay = dayStart,
            endOfDay = dayEnd
        )

        val bookings = bookingRepository.findAllByMachineAndDay(
            machineId = machineId,
            startOfDay = dayStart,
            endOfDay = dayEnd
        )

        val availableSlots = daySlots.filter { slot ->

            if (slot.isBooked) {
                return@filter false
            }

            val candidateStart = slot.startTime
            val candidateEnd = candidateStart.plusMinutes(totalDurationMinutes.toLong())

            val overlapsExistingBooking = bookings.any { booking ->
                candidateStart < booking.endTime && candidateEnd > booking.startTime
            }

            !overlapsExistingBooking
        }

        return availableSlots.map {
            SlotResponse(
                id = it.id,
                startTime = it.startTime.toString(),
                endTime = it.endTime.toString(),
                isBooked = it.isBooked
            )
        }
    }
}