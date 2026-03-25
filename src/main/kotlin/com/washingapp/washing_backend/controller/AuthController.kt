package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.AuthResponse
import com.washingapp.washing_backend.dto.LoginRequest
import com.washingapp.washing_backend.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): AuthResponse {
        val (token, userId) = authService.login(request)

        return AuthResponse(
            token = token,
            userId = userId
        )
    }
}