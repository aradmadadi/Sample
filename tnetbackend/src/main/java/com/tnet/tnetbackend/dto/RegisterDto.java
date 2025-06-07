package com.tnet.tnetbackend.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String role; // نام نقشی که می‌خواهیم به کاربر بدهیم، مثلا "HUB_ADMIN"
}