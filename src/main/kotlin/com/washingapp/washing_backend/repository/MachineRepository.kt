package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Machine
import org.springframework.data.jpa.repository.JpaRepository

interface MachineRepository : JpaRepository<Machine, Long> {
    fun findAllByIdIn(ids: List<Long>): List<Machine>
}