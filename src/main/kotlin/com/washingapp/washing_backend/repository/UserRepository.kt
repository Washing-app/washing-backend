package com.washingapp.washing_backend.repository

import com.washingapp.washing_backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {

    fun findByPhone(phone: String): User?
}