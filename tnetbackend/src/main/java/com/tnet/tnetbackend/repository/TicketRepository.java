package com.tnet.tnetbackend.repository;

import com.tnet.tnetbackend.entity.Ticket;
import com.tnet.tnetbackend.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // متدی برای پیدا کردن یک نوبت از طریق کد رهگیری منحصر به فرد آن
    Optional<Ticket> findByTrackingCode(String trackingCode);

    // متدی برای شمارش تعداد نوبت‌های در انتظار برای یک خدمت خاص
    long countByServiceIdAndStatus(Long serviceId, TicketStatus status);
}