package com.pulsesphere.auth.repository

import com.pulsesphere.auth.domain.AuthUser
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface AuthUserRepository : ReactiveMongoRepository<AuthUser, String> {
    fun findByEmail(email: String): Mono<AuthUser>
}
