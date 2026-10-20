package com.queuewise.api.service;

import com.queuewise.api.service.dto.CreateServiceRequest;
import com.queuewise.api.service.dto.ServiceResponse;
import com.queuewise.api.service.dto.UpdateServiceRequest;
import com.queuewise.api.service.dto.UpdateServiceStatusRequest;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

        @Autowired
        private ServiceManagementService serviceManagementService;

        @PostMapping
        public ResponseEntity<ServiceResponse> createService(
                        @Valid @RequestBody CreateServiceRequest request) {
                Service savedService = serviceManagementService.createService(
                                request.name(),
                                request.description(),
                                request.defaultDurationMinutes());

                ServiceResponse response = new ServiceResponse(
                                savedService.getId(),
                                savedService.getName(),
                                savedService.getDescription(),
                                savedService.getDefaultDurationMinutes(),
                                savedService.isActive(),
                                savedService.getCreatedAt(),
                                savedService.getUpdatedAt());

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping
        public ResponseEntity<List<ServiceResponse>> getAllServices() {

                List<ServiceResponse> response = serviceManagementService
                                .getAllServices()
                                .stream()
                                .map(service -> new ServiceResponse(
                                                service.getId(),
                                                service.getName(),
                                                service.getDescription(),
                                                service.getDefaultDurationMinutes(),
                                                service.isActive(),
                                                service.getCreatedAt(),
                                                service.getUpdatedAt()))
                                .toList();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ServiceResponse> getServiceById(
                        @PathVariable("id") Long id) {
                Service service = serviceManagementService.getServiceById(id);

                ServiceResponse response = new ServiceResponse(
                                service.getId(),
                                service.getName(),
                                service.getDescription(),
                                service.getDefaultDurationMinutes(),
                                service.isActive(),
                                service.getCreatedAt(),
                                service.getUpdatedAt());

                return ResponseEntity.ok(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ServiceResponse> updateService(
                        @PathVariable("id") Long id,
                        @Valid @RequestBody UpdateServiceRequest request) {
                Service updatedService = serviceManagementService.updateService(
                                id,
                                request.name(),
                                request.description(),
                                request.defaultDurationMinutes());

                ServiceResponse response = new ServiceResponse(
                                updatedService.getId(),
                                updatedService.getName(),
                                updatedService.getDescription(),
                                updatedService.getDefaultDurationMinutes(),
                                updatedService.isActive(),
                                updatedService.getCreatedAt(),
                                updatedService.getUpdatedAt());

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteService(
                        @PathVariable("id") Long id) {
                serviceManagementService.deleteService(id);
                return ResponseEntity.noContent().build();
        }

        @PatchMapping("/{id}/status")
        public ResponseEntity<ServiceResponse> updateServiceStatus(
                        @PathVariable("id") Long id,
                        @Valid @RequestBody UpdateServiceStatusRequest request) {
                Service updatedService = serviceManagementService.updateServiceStatus(id, request.active());
                ServiceResponse response = new ServiceResponse(
                                updatedService.getId(),
                                updatedService.getName(),
                                updatedService.getDescription(),
                                updatedService.getDefaultDurationMinutes(),
                                updatedService.isActive(),
                                updatedService.getCreatedAt(),
                                updatedService.getUpdatedAt());
                return ResponseEntity.ok(response);
        }
}