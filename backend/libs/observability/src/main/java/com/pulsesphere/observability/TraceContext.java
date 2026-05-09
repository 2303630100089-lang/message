package com.pulsesphere.observability;

public record TraceContext(String traceId, String spanId) {}
