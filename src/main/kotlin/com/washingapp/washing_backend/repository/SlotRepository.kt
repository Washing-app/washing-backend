package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.Slot
import org.springframework.data.jpa.repository.JpaRepository

interface SlotRepository : JpaRepository<Slot, Long>