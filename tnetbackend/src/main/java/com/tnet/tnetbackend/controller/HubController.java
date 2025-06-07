package com.tnet.tnetbackend.controller;

import com.tnet.tnetbackend.dto.HubDto;
import com.tnet.tnetbackend.service.HubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubs")
public class HubController {

    private final HubService hubService;

    public HubController(HubService hubService) {
        this.hubService = hubService;
    }

    // API برای ساخت کانون جدید
    @PostMapping
    @PreAuthorize("hasAuthority('COMPANY_ADMIN')")
    public ResponseEntity<HubDto> createHub(@RequestBody HubDto hubDto) {
        HubDto savedHub = hubService.createHub(hubDto);
        return new ResponseEntity<>(savedHub, HttpStatus.CREATED);
    }

    // --- API جدید برای انتصاب مدیر ---
    // مثلا: PUT /api/hubs/1/manager/2  یعنی کاربر شماره ۲ را مدیر کانون شماره ۱ کن
    @PutMapping("/{hubId}/manager/{userId}")
    @PreAuthorize("hasAuthority('COMPANY_ADMIN')") // فقط مدیر شرکت می‌تواند این کار را بکند
    public ResponseEntity<HubDto> assignManager(@PathVariable Long hubId, @PathVariable Long userId) {
        HubDto updatedHub = hubService.assignManager(hubId, userId);
        return ResponseEntity.ok(updatedHub);
    }
}