package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.AttendantDto;
import com.tnet.tnetbackend.entity.Attendant;
import com.tnet.tnetbackend.entity.Employer;
import com.tnet.tnetbackend.entity.Service; // این import را اضافه کنید
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.AttendantRepository;
import com.tnet.tnetbackend.repository.EmployerRepository;
import com.tnet.tnetbackend.repository.ServiceRepository; // این import را اضافه کنید
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component; // به جای Service از این استفاده کنید
import org.springframework.web.server.ResponseStatusException;

@Component // از @Service به @Component تغییر دادیم چون نام کلاس با سرویس دیگرمان تداخل دارد
public class AttendantService {

    private final AttendantRepository attendantRepository;
    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository; // این را اضافه می‌کنیم

    public AttendantService(AttendantRepository attendantRepository, EmployerRepository employerRepository, UserRepository userRepository, ServiceRepository serviceRepository) {
        this.attendantRepository = attendantRepository;
        this.employerRepository = employerRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
    }

    public AttendantDto createAttendant(AttendantDto attendantDto) {
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User employerManager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer Manager not found."));

        Employer managedEmployer = employerRepository.findByManager(employerManager)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager of any employer."));

        User attendantUser = userRepository.findById(attendantDto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User to be assigned as attendant not found."));

        Attendant attendant = new Attendant();
        attendant.setUser(attendantUser);
        attendant.setCounterNumber(attendantDto.getCounterNumber());
        attendant.setEmployer(managedEmployer);
        attendant.setActive(true);

        Attendant savedAttendant = attendantRepository.save(attendant);

        return mapToDto(savedAttendant);
    }

    // --- متد جدید برای تخصیص خدمت به متصدی ---
    public AttendantDto assignServiceToAttendant(Long attendantId, Long serviceId) {
        // پیدا کردن مدیر کارفرمایی که لاگین کرده و دفتر او
        String managerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User employerManager = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employer Manager not found."));
        Employer managedEmployer = employerRepository.findByManager(employerManager)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a manager of any employer."));

        // پیدا کردن متصدی و خدمت مورد نظر
        Attendant attendant = attendantRepository.findById(attendantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendant not found."));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found."));

        // چک امنیتی: آیا متصدی و خدمت هر دو متعلق به دفتر این مدیر هستند؟
        if(!attendant.getEmployer().equals(managedEmployer) || !service.getEmployer().equals(managedEmployer)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Attendant or Service does not belong to your employer.");
        }

        // اضافه کردن خدمت به لیست خدمات متصدی و ذخیره
        attendant.getServices().add(service);
        Attendant updatedAttendant = attendantRepository.save(attendant);

        return mapToDto(updatedAttendant);
    }

    private AttendantDto mapToDto(Attendant attendant) {
        AttendantDto dto = new AttendantDto();
        dto.setAttendantId(attendant.getId());
        dto.setUserId(attendant.getUser().getId());
        dto.setFirstName(attendant.getUser().getFirstName());
        dto.setLastName(attendant.getUser().getLastName());
        dto.setEmail(attendant.getUser().getEmail());
        dto.setCounterNumber(attendant.getCounterNumber());
        dto.setActive(attendant.isActive());
        return dto;
    }
}