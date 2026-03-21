package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Slot
import com.washingapp.washing_backend.repository.MachineRepository
import com.washingapp.washing_backend.repository.SlotRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class SlotSchedulerService(
    private val slotRepository: SlotRepository,
    private val machineRepository: MachineRepository
) {

    private val SLOT_INTERVAL_MINUTES = 10
    private val DAYS_AHEAD = 7

    /**
     * Проверяем каждые 30 минут
     */
    @Scheduled(fixedRate = 30 * 60 * 1000)
    fun ensureSlotsForNextWeek() {

        val machines = machineRepository.findAll()

        for (machine in machines) {

            val lastSlot = slotRepository.findTopByMachineIdOrderByStartTimeDesc(machine.id)

            val startDate = if (lastSlot == null) {
                LocalDate.now()
            } else {
                lastSlot.startTime.toLocalDate()
            }

            val targetDate = LocalDate.now().plusDays(DAYS_AHEAD.toLong())

            var date = startDate

            while (date.isBefore(targetDate) || date.isEqual(targetDate)) {

                generateSlotsForDay(machine.id, date)

                date = date.plusDays(1)
            }
        }
    }

    private fun generateSlotsForDay(machineId: Long, date: LocalDate) {

        val machine = machineRepository.findById(machineId).orElseThrow()

        var time = date.atStartOfDay()

        repeat(144) {

            val slot = Slot(
                machine = machine,
                startTime = time,
                endTime = time.plusMinutes(SLOT_INTERVAL_MINUTES.toLong()),
            )

            slotRepository.save(slot)

            time = time.plusMinutes(SLOT_INTERVAL_MINUTES.toLong())
        }
    }
}