package com.pulsesphere.user.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties("security")
data class JwtProperties(
    @field:NotBlank val jwtSecret: String,
    @field:NotBlank val issuer: String,
) {
    init {
        require(jwtSecret.length >= 32) { "security.jwt-secret must be at least 32 characters" }
    }
}
