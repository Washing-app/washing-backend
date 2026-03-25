package com.washingapp.washing_backend.service

import com.washingapp.washing_backend.dto.LoginRequest
import com.washingapp.washing_backend.repository.UserRepository
import com.washingapp.washing_backend.security.JwtService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    fun login(request: LoginRequest): Pair<String, String> {
        val user = userRepository.findByPhone(request.phone)
            ?: throw RuntimeException("User not found")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw RuntimeException("Invalid password")
        }
        val token = jwtService.generateToken(user.id)

        return token to user.id.toString()
    }
}