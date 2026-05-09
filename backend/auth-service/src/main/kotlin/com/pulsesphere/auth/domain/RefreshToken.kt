package com.pulsesphere.auth.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("refresh_tokens")
data class RefreshToken(
    @Id val id: String? = null,
    @Indexed val userId: String,
    @Indexed(unique = true) val tokenHash: String,
    val expiresAt: Instant,
    val createdAt: Instant = Instant.now(),
    val revokedAt: Instant? = null,
)
