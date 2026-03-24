package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.dto.MachineResponse
import com.washingapp.washing_backend.dto.SlotResponse
import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MachineService(
    private val machineRepository: MachineRepository,
    private val slotRepository: SlotRepository
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

    fun getAvailableSlots(machineId: Long, date: LocalDate): List<SlotResponse> {
        val start = date.atStartOfDay()
        val end = date.plusDays(1).atStartOfDay()

        return slotRepository.findAvailableSlotsByMachineAndDateRange(machineId, start, end)
            .map {
                SlotResponse(
                    id = it.id,
                    startTime = it.startTime.toString(),
                    endTime = it.endTime.toString(),
                    isBooked = it.isBooked
                )
            }
    }
}