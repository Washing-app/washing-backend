package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.entity.Slot
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SlotSchedulerService(
    private val slotRepository: SlotRepository,
    private val machineRepository: MachineRepository
) {

    private val DAYS_AHEAD = 7

    @Scheduled(fixedRate = 30 * 60 * 1000)
    fun ensureSlotsForNextWeek() {
        val today = LocalDate.now()
        val targetDate = today.plusDays(DAYS_AHEAD.toLong())

        val machines = machineRepository.findAll()

        for (machine in machines) {
            val lastSlot = slotRepository.findTopByMachineIdOrderByStartTimeDesc(machine.id)

            val startDate: LocalDate = if (lastSlot != null) {
                lastSlot.startTime.toLocalDate().plusDays(1)
            } else {
                today
            }

            var date = startDate
            while (!date.isAfter(targetDate)) {
                generateSlotsForDay(machine, date)
                date = date.plusDays(1)
            }
        }
    }

    private fun generateSlotsForDay(machine: Machine, date: LocalDate) {
        val startOfDay = date.atStartOfDay()
        val endOfDay = date.plusDays(1).atStartOfDay()

        var current = startOfDay
        val slotsToSave = mutableListOf<Slot>()

        while (current.isBefore(endOfDay)) {
            val exists = slotRepository.existsByMachineIdAndStartTime(machine.id, current)

            if (!exists) {
                slotsToSave.add(
                    Slot(
                        machine = machine,
                        startTime = current,
                        endTime = current.plusMinutes(10),
                        isBooked = false
                    )
                )
            }

            current = current.plusMinutes(10)
        }

        if (slotsToSave.isNotEmpty()) {
            slotRepository.saveAll(slotsToSave)
        }
    }
}