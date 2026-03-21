package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.WashType
import com.washingapp.washing_backend.repository.WashTypeRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WashTypeService(
    private val washTypeRepository: WashTypeRepository
) {

    fun create(
        name: String,
        durationMinutes: Int,
        price: BigDecimal,
        temperature: Int,
        spinSpeed: Int,
        description: String
    ): WashType {

        if (durationMinutes <= 0) {
            throw RuntimeException("Duration must be positive")
        }

        if (price <= BigDecimal.ZERO) {
            throw RuntimeException("Price must be positive")
        }

        if (temperature !in 0..95) {
            throw RuntimeException("Invalid temperature value")
        }

        if (spinSpeed !in 400..1600) {
            throw RuntimeException("Invalid spin speed value")
        }

        val washType = WashType(
            name = name,
            durationMinutes = durationMinutes,
            price = price,
            temperature = temperature,
            spinSpeed = spinSpeed,
            description = description
        )

        return washTypeRepository.save(washType)
    }

    fun getAll(): List<WashType> =
        washTypeRepository.findAll()
}