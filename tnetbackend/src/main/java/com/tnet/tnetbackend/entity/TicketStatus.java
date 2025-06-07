package com.tnet.tnetbackend.entity;

public enum TicketStatus {
    WAITING,    // در انتظار
    ACTIVE,     // فعال (فراخوانی شده و در حال انجام)
    DONE,       // تمام شده
    CANCELLED   // لغو شده
}