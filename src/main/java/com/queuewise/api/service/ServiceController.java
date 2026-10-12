package com.queuewise.api.service;

import com.queuewise.api.service.dto.CreateServiceRequest;
import com.queuewise.api.service.dto.ServiceResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    @Autowired
    private ServiceManagementService serviceManagementService;

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody CreateServiceRequest request
    ) {
        Service savedService = serviceManagementService.createService(
                request.name(),
                request.description(),
                request.defaultDurationMinutes()
        );

        ServiceResponse response = new ServiceResponse(
                savedService.getId(),
                savedService.getName(),
                savedService.getDescription(),
                savedService.getDefaultDurationMinutes(),
                savedService.isActive(),
                savedService.getCreatedAt(),
                savedService.getUpdatedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}