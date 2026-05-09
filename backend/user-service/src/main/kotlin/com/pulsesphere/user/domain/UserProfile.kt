package com.pulsesphere.user.domain

import java.time.Instant
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("user_profiles")
data class UserProfile(
    @Id val id: String? = null,
    @Indexed(unique = true) val userId: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val bio: String? = null,
    val status: String = "active",
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now(),
)
