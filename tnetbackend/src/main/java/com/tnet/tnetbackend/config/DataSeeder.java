package com.tnet.tnetbackend.config;

import com.tnet.tnetbackend.entity.Role;
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.RoleRepository;
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // سازنده را آپدیت می‌کنیم تا موارد جدید را دریافت کند
    public DataSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles(); // متد برای ساخت نقش‌ها
        seedAdminUser(); // متد برای ساخت کاربر ادمین
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            System.out.println("Seeding initial roles...");
            List<Role> roles = Arrays.asList(
                    new Role("COMPANY_ADMIN"), new Role("HUB_ADMIN"),
                    new Role("EMPLOYER_ADMIN"), new Role("ATTENDANT"), new Role("CUSTOMER")
            );
            roleRepository.saveAll(roles);
            System.out.println("Roles seeded successfully.");
        }
    }

    private void seedAdminUser() {
        // چک می‌کنیم که آیا کاربر ادمین از قبل وجود دارد یا نه
        if (userRepository.findByEmail("aradmadadi@gmail.com").isEmpty()) {
            System.out.println("Admin user not found, creating one...");

            // نقش ادمین شرکت را از دیتابیس پیدا می‌کنیم
            Role adminRole = roleRepository.findByName("COMPANY_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Error: COMPANY_ADMIN role not found."));

            User adminUser = new User();
            adminUser.setEmail("aradmadadi@gmail.com");
            adminUser.setUsername("aradmadadi@gmail.com"); // یا هر نام کاربری دیگر
            // رمز عبور را هش کرده و ذخیره می‌کنیم
            adminUser.setPassword(passwordEncoder.encode("Emd137159"));
            adminUser.setRoles(Set.of(adminRole));
            adminUser.setActive(true);

            userRepository.save(adminUser);
            System.out.println("COMPANY_ADMIN user created successfully.");
        }
    }

    // متد findByName را به اینترفیس RoleRepository اضافه کن
    // برو به فایل RoleRepository.java و این خط را به آن اضافه کن:
    /*
    Optional<Role> findByName(String name);
    */
}