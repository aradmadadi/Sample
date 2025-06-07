package com.tnet.tnetbackend.security;

import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service // این کلاس را به عنوان یک سرویس به Spring معرفی می‌کند
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // این متد توسط Spring Security برای پیدا کردن کاربر فراخوانی می‌شود
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ما کاربر را با ایمیل از دیتابیس خودمان پیدا می‌کنیم
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email: " + email));

        // نقش‌های کاربر را به فرمتی که Spring Security می‌فهمد تبدیل می‌کنیم
        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        // یک کاربر قابل فهم برای Spring Security را برمی‌گردانیم
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}