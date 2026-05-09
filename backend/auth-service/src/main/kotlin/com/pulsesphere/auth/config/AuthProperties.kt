package com.pulsesphere.auth.config

import jakarta.validation.constraints.NotBlank
import java.time.Duration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("auth")
data class AuthProperties(
    @field:NotBlank val jwtSecret: String,
    @field:NotBlank val issuer: String,
    val accessTokenTtl: Duration,
    val refreshTokenTtl: Duration,
) {
    init {
        require(jwtSecret.length >= 32) { "auth.jwt-secret must be at least 32 characters" }
    }
}
