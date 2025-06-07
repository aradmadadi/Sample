package com.tnet.tnetbackend.controller;

import com.tnet.tnetbackend.dto.EmployerDto;
import com.tnet.tnetbackend.service.EmployerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;

    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HUB_ADMIN')")
    public ResponseEntity<EmployerDto> createEmployer(@RequestBody EmployerDto employerDto) {
        EmployerDto createdEmployer = employerService.createEmployer(employerDto);
        return new ResponseEntity<>(createdEmployer, HttpStatus.CREATED);
    }

    // API جدید برای انتصاب مدیر به کارفرما
    @PutMapping("/{employerId}/manager/{userId}")
    // فقط مدیر کانونی که این کارفرما را ساخته، می‌تواند برایش مدیر تعیین کند (این منطق را بعدا اضافه می‌کنیم)
    // فعلا همین سطح دسترسی کافی است
    @PreAuthorize("hasAuthority('HUB_ADMIN')")
    public ResponseEntity<EmployerDto> assignManager(@PathVariable Long employerId, @PathVariable Long userId) {
        EmployerDto updatedEmployer = employerService.assignManager(employerId, userId);
        return ResponseEntity.ok(updatedEmployer);
    }
}