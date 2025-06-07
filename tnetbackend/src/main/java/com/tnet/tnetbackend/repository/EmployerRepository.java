package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Employer;
import com.tnet.tnetbackend.entity.User; // این import را اضافه کنید
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional; // این import را اضافه کنید

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    // متد جدید برای پیدا کردن کارفرما از روی مدیر آن
    Optional<Employer> findByManager(User manager);
}