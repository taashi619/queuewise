package com.queuewise.api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class ServiceManagementService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Transactional
    public Service createService(
            String name,
            String description,
            Integer defaultDurationMinutes
    ) {
        if (name == null || name.trim().length() < 2
                || name.trim().length() > 100) {
            throw new IllegalArgumentException(
                    "Service name must contain between 2 and 100 characters."
            );
        }

        if (defaultDurationMinutes == null || defaultDurationMinutes <= 0) {
            throw new IllegalArgumentException(
                    "Service duration must be greater than zero."
            );
        }

        Service service = new Service(
                name.trim(),
                description,
                defaultDurationMinutes
        );

        return serviceRepository.save(service);
    }
}