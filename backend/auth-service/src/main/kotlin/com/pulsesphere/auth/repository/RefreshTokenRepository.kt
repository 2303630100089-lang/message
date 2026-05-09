package com.pulsesphere.auth.repository

import com.pulsesphere.auth.domain.RefreshToken
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface RefreshTokenRepository : ReactiveMongoRepository<RefreshToken, String> {
    fun findByTokenHash(tokenHash: String): Mono<RefreshToken>
}
