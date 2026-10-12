package com.queuewise.api.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateServiceRequest(

        @NotBlank(message = "Service name is required.")
        @Size(
                min = 2,
                max = 100,
                message = "Service name must contain between 2 and 100 characters."
        )
        String name,

        String description,

        @NotNull(message = "Service duration is required.")
        @Positive(message = "Service duration must be greater than zero.")
        Integer defaultDurationMinutes

) {
}