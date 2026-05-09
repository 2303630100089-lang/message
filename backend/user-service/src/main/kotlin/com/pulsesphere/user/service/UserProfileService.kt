package com.pulsesphere.user.service

import com.pulsesphere.common.events.EventEnvelope
import com.pulsesphere.common.events.UserRegisteredEvent
import com.pulsesphere.user.domain.UserProfile
import com.pulsesphere.user.repository.UserProfileRepository
import java.time.Instant
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
) {
    suspend fun getProfile(userId: String): UserProfileResponse {
        val profile = userProfileRepository.findByUserId(userId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Profile not found")
        return profile.toResponse()
    }

    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): UserProfileResponse {
        val profile = userProfileRepository.findByUserId(userId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Profile not found")
        val updated = profile.copy(
            displayName = request.displayName ?: profile.displayName,
            avatarUrl = request.avatarUrl ?: profile.avatarUrl,
            bio = request.bio ?: profile.bio,
            updatedAt = Instant.now(),
        )
        return userProfileRepository.save(updated).awaitSingle().toResponse()
    }

    suspend fun createProfile(event: EventEnvelope<UserRegisteredEvent>) {
        val existing = userProfileRepository.findByUserId(event.payload.userId).awaitSingleOrNull()
        if (existing != null) return
        userProfileRepository.save(
            UserProfile(
                userId = event.payload.userId,
                displayName = event.payload.email.substringBefore("@"),
            ),
        ).awaitSingle()
    }
}

data class UpdateProfileRequest(
    val displayName: String?,
    val avatarUrl: String?,
    val bio: String?,
)

data class UserProfileResponse(
    val userId: String,
    val displayName: String,
    val avatarUrl: String?,
    val bio: String?,
    val status: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)

private fun UserProfile.toResponse() = UserProfileResponse(
    userId = userId,
    displayName = displayName,
    avatarUrl = avatarUrl,
    bio = bio,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
