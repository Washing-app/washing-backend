package com.washingapp.washing_backend.controller

import com.washingapp.washing_backend.dto.UserCreateRequest
import com.washingapp.washing_backend.entity.User
import com.washingapp.washing_backend.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    fun create(@RequestBody request: UserCreateRequest): User {
        return userService.create(
            name = request.name,
            phone = request.phone,
            rawPassword = request.password
        )
    }

    @GetMapping
    fun getAll(): List<User> =
        userService.getAll()
}