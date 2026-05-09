package com.pulsesphere.auth.controller

import com.pulsesphere.auth.service.AuthResponse
import com.pulsesphere.auth.service.AuthService
import com.pulsesphere.auth.service.LoginRequest
import com.pulsesphere.auth.service.RefreshRequest
import com.pulsesphere.auth.service.RegisterRequest
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun register(@Valid @RequestBody request: RegisterRequest): AuthResponse =
        authService.register(request)

    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody request: LoginRequest): AuthResponse =
        authService.login(request)

    @PostMapping("/refresh")
    suspend fun refresh(@Valid @RequestBody request: RefreshRequest): AuthResponse =
        authService.refresh(request)
}
