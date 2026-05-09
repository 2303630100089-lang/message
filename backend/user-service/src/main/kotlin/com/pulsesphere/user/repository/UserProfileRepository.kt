package com.pulsesphere.user.repository

import com.pulsesphere.user.domain.UserProfile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserProfileRepository : ReactiveMongoRepository<UserProfile, String> {
    fun findByUserId(userId: String): Mono<UserProfile>
}
