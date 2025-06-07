package com.tnet.tnetbackend.dto;

import lombok.Data;

@Data
public class CreateTicketRequestDto {
    private Long serviceId;
    private String customerDisplayName; // نام نمایشی برای مشتریان حضوری و ناشناس
}