package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.entity.User
import com.washingapp.washing_backend.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun create(name: String, phone: String, rawPassword: String): User {

        if (userRepository.findByPhone(phone) != null) {
            throw RuntimeException("User with this phone already exists")
        }

        val user = User(
            id = UUID.randomUUID(),
            name = name,
            phone = phone,
            passwordHash = passwordEncoder.encode(rawPassword).toString()
        )

        return userRepository.save(user)
    }

    fun getAll(): List<User> =
        userRepository.findAll()
}