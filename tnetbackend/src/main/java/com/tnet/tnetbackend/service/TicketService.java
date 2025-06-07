package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.CreateTicketRequestDto;
import com.tnet.tnetbackend.dto.TicketDto;
import com.tnet.tnetbackend.entity.Service;
import com.tnet.tnetbackend.entity.Ticket;
import com.tnet.tnetbackend.entity.TicketStatus;
import com.tnet.tnetbackend.repository.EmployerRepository;
import com.tnet.tnetbackend.repository.ServiceRepository;
import com.tnet.tnetbackend.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
public class TicketService {

    private final TicketRepository ticketRepository;
    private final ServiceRepository serviceRepository;
    private final EmployerRepository employerRepository; // این ریپازیتوری از قبل وجود دارد

    public TicketService(TicketRepository ticketRepository, ServiceRepository serviceRepository, EmployerRepository employerRepository) {
        this.ticketRepository = ticketRepository;
        this.serviceRepository = serviceRepository;
        this.employerRepository = employerRepository;
    }

    @Transactional // این اطمینان می‌دهد که تمام عملیات‌ها با هم انجام یا لغو شوند
    public TicketDto createTicket(CreateTicketRequestDto requestDto) {
        // ۱. پیدا کردن خدمتی که مشتری انتخاب کرده
        Service service = serviceRepository.findById(requestDto.getServiceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        // ۲. چک کردن اینکه آیا نوبت‌دهی برای این دفتر فعال است یا نه
        if (!service.getEmployer().isQrCodeActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ticket issuing is currently disabled for this office.");
        }

        // ۳. محاسبه شماره نوبت جدید
        // فعلا برای سادگی، تعداد کل نوبت‌های این خدمت را + ۱ می‌کنیم
        // در آینده این منطق را برای محاسبه روزانه، پیچیده‌تر خواهیم کرد
        long currentQueueSize = ticketRepository.count(); // TODO: Implement daily counter logic
        int newQueueNumber = (int) currentQueueSize + 1;

        // ۴. ساختن یک نوبت جدید
        Ticket newTicket = new Ticket();
        newTicket.setService(service);
        newTicket.setEmployer(service.getEmployer());
        newTicket.setTrackingCode(UUID.randomUUID().toString()); // تولید کد رهگیری یکتا
        newTicket.setQueueNumber(newQueueNumber);
        newTicket.setStatus(TicketStatus.WAITING);
        newTicket.setCustomerDisplayName(requestDto.getCustomerDisplayName());
        newTicket.setOnlineBooking(false); // این نوبت حضوری است

        // ۵. ذخیره نوبت در دیتابیس
        Ticket savedTicket = ticketRepository.save(newTicket);

        // ۶. تبدیل نوبت ذخیره شده به DTO برای ارسال به مشتری
        return mapToDto(savedTicket);
    }

    private TicketDto mapToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setTrackingCode(ticket.getTrackingCode());
        dto.setQueueNumber(ticket.getQueueNumber());
        dto.setStatus(ticket.getStatus());
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setServiceName(ticket.getService().getName());
        dto.setEmployerName(ticket.getEmployer().getName());
        return dto;
    }
}