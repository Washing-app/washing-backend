package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.Machine
import com.washingapp.washing_backend.repository.MachineRepository
import org.springframework.stereotype.Service

@Service
class MachineService(
    private val machineRepository: MachineRepository
) {

    fun create(machine: Machine): Machine {
        return machineRepository.save(machine)
    }

    fun getAll(): List<Machine> {
        return machineRepository.findAll()
    }
}