package com.tnet.tnetbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // این import را اضافه کنید
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('HUB_ADMIN')") // <-- این قفل اضافه شد: فقط مدیر کانون دسترسی دارد
    public ResponseEntity<String> getAuthenticatedHello() {
        return ResponseEntity.ok("Hello HUB_ADMIN! Your Token is working.");
    }
}