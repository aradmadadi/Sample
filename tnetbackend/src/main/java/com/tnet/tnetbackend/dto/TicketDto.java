package com.tnet.tnetbackend.dto;

import com.tnet.tnetbackend.entity.TicketStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Long id;
    private String trackingCode;
    private int queueNumber;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private String serviceName;
    private String employerName;
    // بعدا فیلدهایی مثل "زمان تخمینی انتظار" را هم به اینجا اضافه خواهیم کرد
}