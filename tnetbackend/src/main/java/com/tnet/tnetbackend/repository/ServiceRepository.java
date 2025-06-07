package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; // این import را اضافه کنید

public interface ServiceRepository extends JpaRepository<Service, Long> {

    // متدی برای پیدا کردن تمام خدمات مربوط به یک کارفرما (دفتر) خاص
    List<Service> findAllByEmployerId(Long employerId);

}