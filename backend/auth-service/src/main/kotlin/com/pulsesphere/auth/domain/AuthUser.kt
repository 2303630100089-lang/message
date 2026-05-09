package com.pulsesphere.auth.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("auth_users")
data class AuthUser(
    @Id val id: String? = null,
    @Indexed(unique = true) val email: String,
    val passwordHash: String,
    val roles: List<String> = listOf("USER"),
    val createdAt: Instant = Instant.now(),
)
