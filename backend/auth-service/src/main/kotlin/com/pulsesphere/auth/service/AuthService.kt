package com.pulsesphere.auth.service

import com.pulsesphere.auth.config.AuthProperties
import com.pulsesphere.auth.domain.AuthUser
import com.pulsesphere.auth.domain.RefreshToken
import com.pulsesphere.auth.repository.AuthUserRepository
import com.pulsesphere.auth.repository.RefreshTokenRepository
import com.pulsesphere.common.events.EventEnvelope
import com.pulsesphere.common.events.Topics
import com.pulsesphere.common.events.UserRegisteredEvent
import com.pulsesphere.common.security.JwtPrincipal
import com.pulsesphere.common.security.JwtTokenService
import com.fasterxml.jackson.databind.ObjectMapper
import java.security.MessageDigest
import java.time.Instant
import java.util.UUID
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authUserRepository: AuthUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authProperties: AuthProperties,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
) {
    private val jwtTokenService = JwtTokenService(authProperties.jwtSecret, authProperties.issuer)

    suspend fun register(request: RegisterRequest): AuthResponse {
        val existing = authUserRepository.findByEmail(request.email).awaitSingleOrNull()
        require(existing == null) { "Email already registered" }
        val user = authUserRepository.save(
            AuthUser(
                email = request.email.lowercase(),
                passwordHash = passwordEncoder.encode(request.password),
            ),
        ).awaitSingle()

        publishUserRegistered(user)
        return issueTokens(user)
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val user = authUserRepository.findByEmail(request.email.lowercase()).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Invalid credentials")
        require(passwordEncoder.matches(request.password, user.passwordHash)) { "Invalid credentials" }
        return issueTokens(user)
    }

    suspend fun refresh(request: RefreshRequest): AuthResponse {
        val principal = jwtTokenService.parseToken(request.refreshToken)
        require(jwtTokenService.tokenType(request.refreshToken) == "refresh") { "Invalid token type" }
        val tokenHash = hashToken(request.refreshToken)
        val stored = refreshTokenRepository.findByTokenHash(tokenHash).awaitSingleOrNull()
            ?: throw IllegalArgumentException("Refresh token not found")
        require(stored.revokedAt == null && stored.expiresAt.isAfter(Instant.now())) { "Refresh token expired" }
        val user = authUserRepository.findById(principal.userId).awaitSingleOrNull()
            ?: throw IllegalArgumentException("User not found")
        return issueTokens(user)
    }

    private suspend fun issueTokens(user: AuthUser): AuthResponse {
        val principal = JwtPrincipal(userId = user.id ?: "", email = user.email, roles = user.roles)
        val accessToken = jwtTokenService.generateAccessToken(principal, authProperties.accessTokenTtl)
        val refreshToken = jwtTokenService.generateRefreshToken(principal, authProperties.refreshTokenTtl)
        val refreshEntity = RefreshToken(
            userId = user.id ?: "",
            tokenHash = hashToken(refreshToken),
            expiresAt = Instant.now().plus(authProperties.refreshTokenTtl),
        )
        refreshTokenRepository.save(refreshEntity).awaitSingle()
        return AuthResponse(
            userId = user.id ?: "",
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresInSeconds = authProperties.accessTokenTtl.seconds,
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(token.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun publishUserRegistered(user: AuthUser) {
        val event = EventEnvelope(
            id = UUID.randomUUID().toString(),
            type = "UserRegistered",
            createdAt = Instant.now(),
            payload = UserRegisteredEvent(
                userId = user.id ?: "",
                email = user.email,
                createdAt = Instant.now(),
            ),
        )
        val payload = objectMapper.writeValueAsString(event)
        kafkaTemplate.send(Topics.USER_EVENTS, user.id, payload)
    }
}

data class RegisterRequest(
    val email: String,
    val password: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class RefreshRequest(
    val refreshToken: String,
)

data class AuthResponse(
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val expiresInSeconds: Long,
)
