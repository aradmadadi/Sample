package com.tnet.tnetbackend.service;

import com.tnet.tnetbackend.dto.LoginRequest;
import com.tnet.tnetbackend.dto.RegisterDto;
import com.tnet.tnetbackend.entity.Role;
import com.tnet.tnetbackend.entity.User;
import com.tnet.tnetbackend.repository.RoleRepository;
import com.tnet.tnetbackend.repository.UserRepository;
import com.tnet.tnetbackend.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    // متد جدید برای ثبت نام کاربر
    public String register(RegisterDto registerDto) {
        // چک می‌کنیم که آیا نام کاربری یا ایمیل تکراری است یا نه
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already taken!");
        }

        // یک کاربر جدید می‌سازیم
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // نقش مورد نظر را از دیتابیس پیدا کرده و به کاربر تخصیص می‌دهیم
        Role userRole = roleRepository.findByName(registerDto.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specified role not found."));
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        return "User registered successfully!";
    }
}