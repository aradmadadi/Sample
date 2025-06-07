package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Hub;
import com.tnet.tnetbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HubRepository extends JpaRepository<Hub, Long> {

    // کوئری را تغییر می‌دهیم تا به جای آبجکت کامل، با شناسه مدیر جستجو کند
    @Query("SELECT h FROM Hub h WHERE h.manager.id = :managerId")
    Optional<Hub> findByManagerId(@Param("managerId") Long managerId);
}