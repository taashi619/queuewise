package com.queuewise.api.common;

import java.time.Instant;
import java.util.Map;

public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}