package com.tnet.tnetbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        System.out.println("\n--- [START] JWT Filter for request: " + request.getRequestURI() + " ---");

        // ۱. گرفتن توکن از هدر درخواست
        String token = getTokenFromRequest(request);
        System.out.println("1. Extracted Token: " + token);

        // ۲. اعتبارسنجی توکن
        if (StringUtils.hasText(token)) {
            if (jwtTokenProvider.validateToken(token)) {
                System.out.println("2. Token is valid.");

                // ۳. گرفتن نام کاربری (ایمیل) از توکن
                String username = jwtTokenProvider.getUsername(token);
                System.out.println("3. Username from token: " + username);

                // ۴. گرفتن اطلاعات کاربر از دیتابیس
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("4. UserDetails loaded for user. Authorities: " + userDetails.getAuthorities());

                // ۵. ساخت یک آبجکت احراز هویت
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // ۶. قرار دادن اطلاعات کاربر در حافظه امنیتی Spring
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("5. Authentication object SET successfully in Security Context.");

            } else {
                System.out.println("2. Token is INVALID.");
            }
        } else {
            System.out.println("1a. Token is null or empty. Skipping JWT processing.");
        }

        filterChain.doFilter(request, response);
        System.out.println("--- [END] JWT Filter for request: " + request.getRequestURI() + " ---\n");
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}