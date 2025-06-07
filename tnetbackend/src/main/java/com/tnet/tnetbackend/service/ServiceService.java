package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.ServiceDto;
import com.tnet.tnetbackend.entity.Employer;
import com.tnet.tnetbackend.entity.Service;
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.EmployerRepository;
import com.tnet.tnetbackend.repository.ServiceRepository;
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;

    public ServiceService(ServiceRepository serviceRepository, EmployerRepository employerRepository, UserRepository userRepository) {
        this.serviceRepository = serviceRepository;
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
    }

    public ServiceDto createService(ServiceDto serviceDto) {
        // ۱. پیدا کردن مدیر کارفرمایی که لاگین کرده
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User employerManager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer Manager not found."));

        // ۲. پیدا کردن دفتری که این کاربر مدیر آن است
        Employer managedEmployer = employerRepository.findByManager(employerManager)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager of any employer."));

        // ۳. ساخت موجودیت خدمت جدید
        Service newService = new Service();
        newService.setName(serviceDto.getName());
        newService.setDescription(serviceDto.getDescription());
        newService.setEstimatedDurationMinutes(serviceDto.getEstimatedDurationMinutes());
        newService.setActive(true);
        newService.setEmployer(managedEmployer); // ۴. اتصال خودکار خدمت به دفتر مدیر

        Service savedService = serviceRepository.save(newService);

        return mapToDto(savedService);
    }

    // متد کمکی برای تبدیل Entity به DTO
    private ServiceDto mapToDto(Service service) {
        ServiceDto dto = new ServiceDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setEstimatedDurationMinutes(service.getEstimatedDurationMinutes());
        dto.setActive(service.isActive());
        dto.setEmployerId(service.getEmployer().getId());
        return dto;
    }
}