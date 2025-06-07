package com.tnet.tnetbackend.config;

import com.tnet.tnetbackend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                // ۱. مسیرهای عمومی
                                .requestMatchers("/api/auth/**", "/error").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/tickets").permitAll() //  قانون جدید برای گرفتن نوبت

                                // ۲. قوانین دسترسی پنل‌های مدیریتی
                                .requestMatchers(HttpMethod.POST, "/api/hubs").hasAuthority("COMPANY_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/hubs/**").hasAuthority("COMPANY_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/employers").hasAuthority("HUB_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/employers/**").hasAuthority("HUB_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/attendants").hasAuthority("EMPLOYER_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/services").hasAuthority("EMPLOYER_ADMIN")

                                // ۳. سایر درخواست‌ها نیاز به احراز هویت دارند
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}