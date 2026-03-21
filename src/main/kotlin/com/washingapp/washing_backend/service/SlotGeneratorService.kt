package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Slot
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import org.springframework.stereotype.Service
import java.time.LocalDate


@Service
class SlotGeneratorService(
    private val slotRepository: SlotRepository,
    private val machineRepository: MachineRepository
) {

    fun generateForDay(machineId: Long, date: LocalDate) {

        val machine = machineRepository.findById(machineId)
            .orElseThrow()

        var time = date.atStartOfDay()

        repeat(144) {

            val slot = Slot(
                machine = machine,
                startTime = time,
                endTime = time.plusMinutes(10)
            )

            slotRepository.save(slot)

            time = time.plusMinutes(10)
        }
    }
}