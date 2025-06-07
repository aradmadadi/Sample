package com.tnet.tnetbackend.dto;

import lombok.Data;

@Data
public class EmployerDto {
    private Long id;
    private String name;
    private String officeCode;
    private String address;
    private String phoneNumber;
    private Long hubId; // این فیلد در پاسخ به کاربر پر خواهد شد
}