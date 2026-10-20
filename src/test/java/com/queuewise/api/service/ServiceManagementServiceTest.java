package com.queuewise.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.queuewise.api.common.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ServiceManagementServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceManagementService serviceManagementService;

    @Test
    void shouldCreateServiceWithTrimmedName() {
        // Arrange
        when(serviceRepository.save(any(Service.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Service.class));

        // Act
        Service result = serviceManagementService.createService(
                "  Brake Adjustment  ",
                "Inspect and adjust bicycle brakes.",
                30);

        // Assert
        assertEquals("Brake Adjustment", result.getName());
        assertEquals(
                "Inspect and adjust bicycle brakes.",
                result.getDescription());
        assertEquals(Integer.valueOf(30), result.getDefaultDurationMinutes());
        assertTrue(result.isActive());

        verify(serviceRepository).save(result);
    }

    @Test
    void shouldRejectNameThatIsTooShortAfterTrimming() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> serviceManagementService.createService(
                        " a ",
                        "Test description.",
                        30));

        // Assert
        assertEquals(
                "Service name must contain between 2 and 100 characters.",
                exception.getMessage());

        verifyNoInteractions(serviceRepository);
    }

    @Test
    void shouldRejectZeroDuration() {
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> serviceManagementService.createService(
                        "Brake Adjustment",
                        "Test description.",
                        0));

        // Assert
        assertEquals(
                "Service duration must be greater than zero.",
                exception.getMessage());

        verifyNoInteractions(serviceRepository);
    }

    @Test
    void shouldDeactivateService() {
        // Arrange
        Long serviceId = 1L;

        Service existingService = new Service(
                "Brake Adjustment",
                "Inspect and adjust bicycle brakes.",
                30);

        assertTrue(existingService.isActive());

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(existingService));

        when(serviceRepository.save(existingService))
                .thenReturn(existingService);

        // Act
        Service result = serviceManagementService.updateServiceStatus(
                serviceId,
                false);

        // Assert
        assertFalse(result.isActive());
        assertSame(existingService, result);

        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository).save(existingService);
    }

    @Test
    void shouldRejectZeroDurationOnUpdate() {

        Long serviceId = 1L;

        Service existingService = new Service(
                "Brake Adjustment",
                "Inspect and adjust bicycle brakes.",
                30);

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(existingService));
        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> serviceManagementService.updateService(
                        1L,
                        "Brake Adjustment",
                        "Test description.",
                        0));

        // Assert
        assertEquals(
                "Service duration must be greater than zero.",
                exception.getMessage());

        verify(serviceRepository).findById(1L);

        verify(serviceRepository, never()).save(any(Service.class));

    }

    @Test
    void shouldReturnServiceWhenIdExists() {
        // Arrange
        Long serviceId = 1L;

        Service existingService = new Service(
                "Brake Adjustment",
                "Inspect and adjust bicycle brakes.",
                30);

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(existingService));

        // Act
        Service result = serviceManagementService.getServiceById(serviceId);

        // Assert
        assertSame(existingService, result);
        assertEquals("Brake Adjustment", result.getName());

        verify(serviceRepository).findById(serviceId);
    }

    @Test
    void shouldThrowExceptionWhenServiceIdDoesNotExist() {
        // Arrange
        Long serviceId = 999L;

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.empty());

        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> serviceManagementService.getServiceById(serviceId));

        // Assert
        assertEquals(
                "Service not found with ID: 999",
                exception.getMessage());

        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository, never()).save(any(Service.class));
    }

    @Test
    void shouldReactivateService() {
        // Arrange: prepare an inactive service
        Long serviceId = 1L;

        Service existingService = new Service(
                "Brake Adjustment",
                "Inspect and adjust bicycle brakes.",
                30);

        existingService.setActive(false);

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.of(existingService));

        when(serviceRepository.save(existingService))
                .thenReturn(existingService);

        // Act: ask the application to activate it
        Service result = serviceManagementService.updateServiceStatus(
                serviceId,
                true);

        // Assert: confirm that it is now active
        assertTrue(result.isActive());

        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository).save(existingService);
    }

    @Test
    void shouldThrowExceptionWhenServiceIdDoesNotExistOnUpdateStatus() {
        // Arrange
        Long serviceId = 999L;

        when(serviceRepository.findById(serviceId))
                .thenReturn(Optional.empty());
        // Act
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> serviceManagementService.updateServiceStatus(serviceId, true));

        // Assert
        assertEquals("Service not found with ID: 999", exception.getMessage());
        verify(serviceRepository).findById(serviceId);
        verify(serviceRepository, never()).save(any(Service.class));
    }

}