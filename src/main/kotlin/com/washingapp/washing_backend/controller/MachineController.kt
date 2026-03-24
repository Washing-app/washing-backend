package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.MachineResponse
import com.washingapp.washing_backend.dto.SlotResponse
import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.service.MachineService
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/machines")
class MachineController(
    private val machineService: MachineService
) {

    @PostMapping
    fun create(@RequestBody machine: Machine): Machine {
        return machineService.create(machine)
    }

    @GetMapping
    fun getAll(): List<Machine> {
        return machineService.getAll()
    }

    @GetMapping("/available")
    fun getAvailableMachines(
        @RequestParam date: LocalDate
    ): List<MachineResponse> {
        return machineService.getAvailableMachines(date)
    }

    @GetMapping("/{machineId}/slots")
    fun getAvailableSlots(
        @PathVariable machineId: Long,
        @RequestParam date: LocalDate
    ): List<SlotResponse> {
        return machineService.getAvailableSlots(machineId, date)
    }
}