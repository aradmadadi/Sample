package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendantRepository extends JpaRepository<Attendant, Long> {
}