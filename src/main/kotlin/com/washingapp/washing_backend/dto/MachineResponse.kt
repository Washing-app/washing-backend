package com.washingapp.washing_backend.dto

data class MachineResponse(
    val id: Long,
    val name: String,
    val status: String,
    val location: String
)
