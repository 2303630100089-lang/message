package com.pulsesphere.security;

import java.time.Instant;

public record TokenClaims(String subject, Instant issuedAt, Instant expiresAt) {}
