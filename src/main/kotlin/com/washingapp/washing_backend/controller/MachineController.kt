package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.service.MachineService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/machines")
class MachineController(
    private val service: MachineService
) {

    @PostMapping
    fun create(@RequestBody machine: Machine): Machine {
        return service.create(machine)
    }

    @GetMapping
    fun getAll(): List<Machine> {
        return service.getAll()
    }
}