package com.washingapp.washing_backend.dto

import java.time.LocalDateTime

data class CreateSlotRequest(
    val machineId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)