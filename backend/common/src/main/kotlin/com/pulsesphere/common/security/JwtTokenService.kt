package com.pulsesphere.common.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.time.Duration
import java.time.Instant
import java.util.Date

data class JwtPrincipal(
    val userId: String,
    val email: String,
    val roles: List<String>,
)

class JwtTokenService(
    secret: String,
    private val issuer: String,
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateAccessToken(principal: JwtPrincipal, ttl: Duration): String =
        generateToken(principal, ttl, "access")

    fun generateRefreshToken(principal: JwtPrincipal, ttl: Duration): String =
        generateToken(principal, ttl, "refresh")

    fun parseToken(token: String): JwtPrincipal {
        val claims = parseClaims(token)
        val roles = claims["roles", List::class.java]?.map { it.toString() } ?: emptyList()
        return JwtPrincipal(
            userId = claims.subject,
            email = claims["email"]?.toString().orEmpty(),
            roles = roles,
        )
    }

    fun tokenType(token: String): String = parseClaims(token)["typ"]?.toString().orEmpty()

    private fun parseClaims(token: String): Claims =
        Jwts.parserBuilder()
            .requireIssuer(issuer)
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

    private fun generateToken(principal: JwtPrincipal, ttl: Duration, type: String): String {
        val now = Instant.now()
        val expiration = now.plus(ttl)
        return Jwts.builder()
            .setIssuer(issuer)
            .setSubject(principal.userId)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiration))
            .claim("email", principal.email)
            .claim("roles", principal.roles)
            .claim("typ", type)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
}
