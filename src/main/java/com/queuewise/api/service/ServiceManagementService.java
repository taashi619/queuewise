package com.queuewise.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.queuewise.api.common.ResourceNotFoundException;

@org.springframework.stereotype.Service
public class ServiceManagementService {

        @Autowired
        private ServiceRepository serviceRepository;

        @Transactional
        public Service createService(
                        String name,
                        String description,
                        Integer defaultDurationMinutes) {
                if (name == null || name.trim().length() < 2
                                || name.trim().length() > 100) {
                        throw new IllegalArgumentException(
                                        "Service name must contain between 2 and 100 characters.");
                }

                if (defaultDurationMinutes == null || defaultDurationMinutes <= 0) {
                        throw new IllegalArgumentException(
                                        "Service duration must be greater than zero.");
                }

                Service service = new Service(
                                name.trim(),
                                description,
                                defaultDurationMinutes);

                return serviceRepository.save(service);
        }

        @Transactional(readOnly = true)
        public List<Service> getAllServices() {
                return serviceRepository.findAll();
        }

        @Transactional(readOnly = true)
        public Service getServiceById(Long id) {
                return serviceRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Service not found with ID: " + id));
        }

        @Transactional
        public Service updateService(
                        Long id,
                        String name,
                        String description,
                        Integer defaultDurationMinutes) {
                Service service = serviceRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Service not found with ID: " + id));

                if (name == null || name.trim().length() < 2
                                || name.trim().length() > 100) {
                        throw new IllegalArgumentException(
                                        "Service name must contain between 2 and 100 characters.");
                }

                if (defaultDurationMinutes == null || defaultDurationMinutes <= 0) {
                        throw new IllegalArgumentException(
                                        "Service duration must be greater than zero.");
                }

                service.setName(name.trim());
                service.setDescription(description);
                service.setDefaultDurationMinutes(defaultDurationMinutes);

                return serviceRepository.save(service);
        }

        @Transactional
        public void deleteService(Long id) {
                
                serviceRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Service not found with ID: " + id));
                serviceRepository.deleteById(id);
        }

        @Transactional
        public Service updateServiceStatus(Long id, Boolean active) {
                Service service = serviceRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Service not found with ID: " + id));
                service.setActive(active);
                return serviceRepository.save(service);
        }
}