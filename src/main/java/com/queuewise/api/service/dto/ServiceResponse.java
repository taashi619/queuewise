package com.queuewise.api.service.dto;

import java.time.Instant;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        Integer defaultDurationMinutes,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}