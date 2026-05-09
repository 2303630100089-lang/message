package com.pulsesphere.shared;

import java.time.Instant;

public record DomainEvent(String type, String aggregateId, Instant occurredAt) {}
