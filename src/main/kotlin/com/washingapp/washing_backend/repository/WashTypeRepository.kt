package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.WashType
import org.springframework.data.jpa.repository.JpaRepository

interface WashTypeRepository : JpaRepository<WashType, Long>