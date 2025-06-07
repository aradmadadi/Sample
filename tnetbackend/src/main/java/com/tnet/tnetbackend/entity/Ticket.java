package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tickets") // نام جدول را به tickets تغییر دادیم
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String trackingCode; // مطابق با tracking_code

    @Column(nullable = false)
    private int queueNumber; // مطابق با queue_number

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status; // مطابق با status و enum جدید

    @Column(nullable = false)
    private boolean isOnlineBooking = false; // مطابق با is_online_booking

    private String customerDisplayName; // مطابق با customer_display_name

    // ---- تایم استمپ‌ها مطابق با اسکیما ----
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // مطابق با created_at

    private LocalDateTime activatedAt; // مطابق با activated_at

    private LocalDateTime completedAt; // مطابق با completed_at

    // ---- روابط با دیگر جدول‌ها ----

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id") // customer_id می‌تواند null باشد
    private User customer; // فعلا به User متصل می‌کنیم، بعدا Customer خواهیم ساخت

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendant_id") // attendant_id می‌تواند null باشد
    private Attendant attendant;
}