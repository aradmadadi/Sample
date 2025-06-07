package com.tnet.tnetbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // خواندن کلید مخفی از application.properties
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    // خواندن زمان انقضا از application.properties
    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // متد اصلی برای ساختن توکن
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key()) // امضا با کلید مخفی
                .compact();
    }

    // یک کلید امن بر اساس رشته مخفی ما می‌سازد
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // نام کاربری را از توکن استخراج می‌کند
    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    // اعتبار توکن را بررسی می‌کند
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parse(token);
            return true;
        } catch (Exception e) {
            // در صورت بروز خطا (مثلا توکن نامعتبر یا منقضی شده)، false برمی‌گرداند
            return false;
        }
    }
}