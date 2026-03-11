package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.WashType
import com.washingapp.washing_backend.repository.WashTypeRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class WashTypeService(
    private val washTypeRepository: WashTypeRepository
) {

    fun create(name: String, durationMinutes: Int, price: BigDecimal): WashType {

        if (durationMinutes <= 0) {
            throw RuntimeException("Duration must be positive")
        }

        if (price <= BigDecimal.ZERO) {
            throw RuntimeException("Price must be positive")
        }

        val washType = WashType(
            name = name,
            durationMinutes = durationMinutes,
            price = price
        )

        return washTypeRepository.save(washType)
    }

    fun getAll(): List<WashType> =
        washTypeRepository.findAll()
}