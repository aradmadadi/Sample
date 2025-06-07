package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "hubs")
public class Hub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    // این فیلد برای نگهداری کاربری که کانون را ساخته، وجود داشت
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_company_user_id", nullable = false)
    private User createdBy;

    // --- بخش اضافه شده ---
    // این فیلد برای نگهداری مدیر کانون اضافه شد
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user_id", unique = true) // هر کاربر فقط می‌تواند مدیر یک کانون باشد
    private User manager;
    // --- پایان بخش اضافه شده ---
}