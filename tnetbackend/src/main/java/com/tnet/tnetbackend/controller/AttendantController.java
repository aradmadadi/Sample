package com.tnet.tnetbackend.controller;

import com.tnet.tnetbackend.dto.AttendantDto;
import com.tnet.tnetbackend.service.AttendantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*; // این import را اضافه کنید

@RestController
@RequestMapping("/api/attendants")
public class AttendantController {

    private final AttendantService attendantService;

    public AttendantController(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYER_ADMIN')")
    public ResponseEntity<AttendantDto> createAttendant(@RequestBody AttendantDto attendantDto) {
        AttendantDto createdAttendant = attendantService.createAttendant(attendantDto);
        return new ResponseEntity<>(createdAttendant, HttpStatus.CREATED);
    }

    // --- API جدید برای تخصیص خدمت ---
    @PostMapping("/{attendantId}/services/{serviceId}")
    @PreAuthorize("hasAuthority('EMPLOYER_ADMIN')")
    public ResponseEntity<AttendantDto> assignService(
            @PathVariable Long attendantId,
            @PathVariable Long serviceId) {

        AttendantDto updatedAttendant = attendantService.assignServiceToAttendant(attendantId, serviceId);
        return ResponseEntity.ok(updatedAttendant);
    }
}