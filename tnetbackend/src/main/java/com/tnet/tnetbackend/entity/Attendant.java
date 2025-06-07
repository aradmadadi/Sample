package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "attendants")
public class Attendant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- فیلد جدید اضافه شده ---
    @Column(name = "national_id", unique = true, length = 11)
    private String nationalId;

    @Column(nullable = false)
    private String counterNumber;

    @Column(nullable = false)
    private boolean isActive = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    private Employer employer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "attendant_services",
            joinColumns = @JoinColumn(name = "attendant_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private Set<Service> services = new HashSet<>();
}