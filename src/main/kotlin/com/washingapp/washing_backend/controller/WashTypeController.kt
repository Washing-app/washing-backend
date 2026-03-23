package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.CreateWashTypeRequest
import com.washingapp.washing_backend.entity.WashType
import com.washingapp.washing_backend.service.WashTypeService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/wash-types")
class WashTypeController(
    private val washTypeService: WashTypeService
) {

    @PostMapping
    fun create(@RequestBody request: CreateWashTypeRequest): WashType {
        return washTypeService.create(
            request.name,
            request.durationMinutes,
            request.price,
            request.temperature,
            request.spinSpeed,
            request.description,
            request.id,
            request.imageLink
        )
    }

    @GetMapping
    fun getAll(): List<WashType> =
        washTypeService.getAll()
}