package com.pulsesphere.chat.config

import com.pulsesphere.common.security.JwtPrincipal
import com.pulsesphere.common.security.JwtTokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono

@Configuration
class SecurityConfig(
    private val jwtProperties: JwtProperties,
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        val jwtService = JwtTokenService(jwtProperties.jwtSecret, jwtProperties.issuer)
        val authenticationManager = JwtAuthenticationManager(jwtService)
        val authenticationWebFilter = AuthenticationWebFilter(authenticationManager).apply {
            setServerAuthenticationConverter(BearerTokenServerAuthenticationConverter())
            setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        }

        return http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .authorizeExchange { exchanges ->
                exchanges.pathMatchers("/actuator/**").permitAll()
                exchanges.anyExchange().authenticated()
            }
            .addFilterAt(authenticationWebFilter, org.springframework.security.web.server.SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }
}

class JwtAuthenticationManager(
    private val jwtTokenService: JwtTokenService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val token = authentication.credentials.toString()
        return Mono.fromCallable { jwtTokenService.parseToken(token) }
            .map { principal ->
                UsernamePasswordAuthenticationToken(
                    principal,
                    token,
                    principal.roles.map { SimpleGrantedAuthority("ROLE_$it") },
                )
            }
    }
}

class BearerTokenServerAuthenticationConverter : ServerAuthenticationConverter {
    override fun convert(exchange: org.springframework.web.server.ServerWebExchange): Mono<Authentication> {
        val header = exchange.request.headers.getFirst("Authorization") ?: return Mono.empty()
        if (!header.startsWith("Bearer ")) return Mono.empty()
        val token = header.removePrefix("Bearer ").trim()
        return Mono.just(UsernamePasswordAuthenticationToken(JwtPrincipal("", "", emptyList()), token))
    }
}
