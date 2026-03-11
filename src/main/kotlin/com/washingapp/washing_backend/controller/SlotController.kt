package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.CreateSlotRequest
import com.washingapp.washing_backend.entity.Slot
import com.washingapp.washing_backend.service.SlotService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/slots")
class SlotController(
    private val slotService: SlotService
) {

    @PostMapping
    fun create(@RequestBody request: CreateSlotRequest): Slot {
        return slotService.create(
            request.machineId,
            request.startTime,
            request.endTime
        )
    }

    @GetMapping
    fun getAll(): List<Slot> =
        slotService.getAll()
}