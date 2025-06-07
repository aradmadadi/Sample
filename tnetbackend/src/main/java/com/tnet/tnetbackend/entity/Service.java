package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "services")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimated_duration_minutes")
    private int estimatedDurationMinutes;

    @Column(nullable = false)
    private boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    // --- بخش اضافه شده برای تعریف رابطه چندبه‌چند ---
    @ManyToMany(mappedBy = "services") // این سمت رابطه، توسط فیلد 'services' در کلاس Attendant مدیریت می‌شود
    private Set<Attendant> attendants = new HashSet<>();
    // --- پایان بخش اضافه شده ---
}