package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Slot
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SlotService(
    private val slotRepository: SlotRepository,
    private val machineRepository: MachineRepository
) {

    fun create(machineId: Long, startTime: LocalDateTime, endTime: LocalDateTime): Slot {

        val machine = machineRepository.findById(machineId)
            .orElseThrow { RuntimeException("Machine not found") }

        if (startTime.isAfter(endTime) || startTime == endTime) {
            throw RuntimeException("Invalid time interval")
        }

        val slot = Slot(
            machine = machine,
            startTime = startTime,
            endTime = endTime
        )

        return slotRepository.save(slot)
    }

    fun getAll(): List<Slot> =
        slotRepository.findAll()
}