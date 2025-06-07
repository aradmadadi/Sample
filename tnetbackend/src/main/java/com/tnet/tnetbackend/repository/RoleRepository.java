package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // این import اضافه می‌شود

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // این خط برای پیدا کردن نقش از روی نام، به اینجا اضافه شد
    Optional<Role> findByName(String name);

}