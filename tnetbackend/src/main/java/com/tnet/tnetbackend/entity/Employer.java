package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal; // برای دقت بالای اعشار

@Data
@NoArgsConstructor
@Entity
@Table(name = "employers")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "office_code", unique = true)
    private String officeCode;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String phoneNumber;

    // --- فیلدهای جدید اضافه شده ---
    @Column(precision = 10, scale = 8) // دقت برای طول جغرافیایی
    private BigDecimal locationLatitude;

    @Column(precision = 11, scale = 8) // دقت برای عرض جغرافیایی
    private BigDecimal locationLongitude;

    @Column(unique = true)
    private String qrCodeIdentifier;

    private boolean isQrCodeActive = true;

    @Column(columnDefinition = "JSON") // برای ذخیره جیسون ساعات کاری
    private String workingHours;
    // --- پایان فیلدهای جدید ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_hub_user_id", nullable = false)
    private User createdBy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user_id", unique = true)
    private User manager;
}