package com.tnet.tnetbackend.controller;

import com.tnet.tnetbackend.dto.CreateTicketRequestDto;
import com.tnet.tnetbackend.dto.TicketDto;
import com.tnet.tnetbackend.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;



    // این API برای گرفتن نوبت جدید توسط مشتری است
    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody CreateTicketRequestDto requestDto) {
        TicketDto createdTicket = ticketService.createTicket(requestDto);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }
}