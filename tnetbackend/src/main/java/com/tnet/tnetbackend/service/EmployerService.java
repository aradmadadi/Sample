package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.EmployerDto;
import com.tnet.tnetbackend.entity.Employer;
import com.tnet.tnetbackend.entity.Hub;
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.EmployerRepository;
import com.tnet.tnetbackend.repository.HubRepository;
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final HubRepository hubRepository;

    public EmployerService(EmployerRepository employerRepository, UserRepository userRepository, HubRepository hubRepository) {
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.hubRepository = hubRepository;
    }

    public EmployerDto createEmployer(EmployerDto employerDto) {
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User hubManager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hub Manager not found."));

        Hub managedHub = hubRepository.findByManagerId(hubManager.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager of any hub."));

        Employer employer = new Employer();
        employer.setName(employerDto.getName());
        employer.setOfficeCode(employerDto.getOfficeCode());
        employer.setAddress(employerDto.getAddress());
        employer.setPhoneNumber(employerDto.getPhoneNumber());
        employer.setHub(managedHub);
        employer.setCreatedBy(hubManager);

        Employer savedEmployer = employerRepository.save(employer);

        return mapToDto(savedEmployer);
    }

    public EmployerDto assignManager(Long employerId, Long userId) {
        // ۱. پیدا کردن کاربری که لاگین کرده (مدیر کانون)
        String loggedInUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User hubManager = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hub Manager not found."));

        // ۲. پیدا کردن کارفرما (دفتر) مورد نظر
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer not found with id: " + employerId));

        // ۳. چک امنیتی مهم: آیا این دفتر متعلق به کانون همین مدیری است که لاگین کرده؟
        if (employer.getHub() == null || !employer.getHub().getManager().equals(hubManager)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to modify this employer.");
        }

        // ۴. پیدا کردن کاربری که قرار است مدیر شود
        User newManager = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to be assigned not found with id: " + userId));

        // ۵. انتصاب مدیر و ذخیره در دیتابیس
        employer.setManager(newManager);
        Employer updatedEmployer = employerRepository.save(employer);

        return mapToDto(updatedEmployer);
    }

    private EmployerDto mapToDto(Employer employer) {
        EmployerDto dto = new EmployerDto();
        dto.setId(employer.getId());
        dto.setName(employer.getName());
        dto.setOfficeCode(employer.getOfficeCode());
        dto.setAddress(employer.getAddress());
        dto.setPhoneNumber(employer.getPhoneNumber());
        if (employer.getHub() != null) {
            dto.setHubId(employer.getHub().getId());
        }
        return dto;
    }
}