package com.tnet.tnetbackend.dto;

import lombok.Data;

@Data
public class AttendantDto {
    // برای درخواست ساخت متصدی
    private Long userId; // شناسه کاربری که قرار است متصدی شود
    private String counterNumber; // شماره باجه

    // فیلدهای اضافی برای پاسخ
    private Long attendantId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
}