package com.tnet.tnetbackend.dto;

import lombok.Data;

@Data
public class ServiceDto {
    private Long id;
    private String name;
    private String description;
    private int estimatedDurationMinutes;
    private boolean isActive;
    private Long employerId; // این فیلد در پاسخ به کاربر پر می‌شود
}