package com.queuewise.api.service.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateServiceStatusRequest(

        @NotNull(message = "Active status is required.")
        Boolean active

) {
}