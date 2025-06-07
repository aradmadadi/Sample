package com.tnet.tnetbackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    // این سازنده را برای راحتی کار در کلاس DataSeeder اضافه می‌کنیم
    public Role(String name) {
        this.name = name;
    }
}