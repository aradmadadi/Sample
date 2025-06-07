package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.HubDto;
import com.tnet.tnetbackend.entity.Hub;
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.HubRepository;
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HubService {

    private final HubRepository hubRepository;
    private final UserRepository userRepository;

    public HubService(HubRepository hubRepository, UserRepository userRepository) {
        this.hubRepository = hubRepository;
        this.userRepository = userRepository;
    }

    // متد برای ساخت یک کانون جدید
    public HubDto createHub(HubDto hubDto) {
        String creatorEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByEmail(creatorEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Creator user not found"));

        Hub hub = new Hub();
        hub.setName(hubDto.getName());
        hub.setCode(hubDto.getCode());
        hub.setDescription(hubDto.getDescription());
        hub.setCreatedBy(creator);

        Hub savedHub = hubRepository.save(hub);

        return mapToDto(savedHub);
    }

    // --- متد جدید برای انتصاب مدیر ---
    public HubDto assignManager(Long hubId, Long userId) {
        // ۱. کانون مورد نظر را پیدا می‌کنیم
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hub not found with id: " + hubId));

        // ۲. کاربری که قرار است مدیر شود را پیدا می‌کنیم
        User newManager = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

        // ۳. کاربر را به عنوان مدیر این کانون ثبت می‌کنیم
        hub.setManager(newManager);
        Hub updatedHub = hubRepository.save(hub);

        return mapToDto(updatedHub);
    }

    // متد کمکی برای تبدیل Entity به DTO
    private HubDto mapToDto(Hub hub) {
        HubDto hubDto = new HubDto();
        hubDto.setId(hub.getId());
        hubDto.setName(hub.getName());
        hubDto.setCode(hub.getCode());
        hubDto.setDescription(hub.getDescription());
        return hubDto;
    }
}