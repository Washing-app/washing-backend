package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.LoginRequest
import com.washingapp.washing_backend.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Map<String, String> {
        val token = authService.login(request)
        return mapOf("token" to token)
    }
}