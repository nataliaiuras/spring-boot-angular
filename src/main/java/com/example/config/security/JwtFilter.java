package com.example.config.security;


import com.example.services.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final ApplicationContext context;

    public JwtFilter(JWTService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//  Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJraWxsIiwiaWF0IjoxNzIzMTgzNzExLCJleHAiOjE3MjMxODM4MTl9.5nf7dRzKRiuGurN2B9dHh_M5xiu73ZzWPr6rbhOTTHs
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        log.info("Auth header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7) {
            token = authHeader.substring(7).trim();
            if (!token.isEmpty() && isValidJwtFormat(token)) {

                log.info("Extracted token: {}...", token.substring(0, Math.min(50, token.length())));

                try {
                    username = jwtService.extractUserName(token);
                    log.info("Extracted username: {}", username);
                } catch (io.jsonwebtoken.MalformedJwtException e) {
                    log.warn("Malformed JWT token: {}", e.getMessage());
                } catch (io.jsonwebtoken.ExpiredJwtException e) {
                    log.warn("Expired JWT token: {}", e.getMessage());
                } catch (io.jsonwebtoken.UnsupportedJwtException e) {
                    log.warn("Unsupported JWT token: {}", e.getMessage());
                } catch (io.jsonwebtoken.security.SignatureException e) {
                    log.warn("Invalid JWT signature: {}", e.getMessage());
                } catch (IllegalArgumentException e) {
                    log.warn("JWT token compact of handler are invalid: {}", e.getMessage());
                } catch (Exception e) {
                    log.warn("Invalid JWT token: {}", e.getMessage());
                }
            } else {
                log.warn("Token is empty or has invalid JWT format. Token: '{}'", token);
            }
        } else {
            log.debug("No valid Authorization header found");

        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(username);

                log.info("Loaded user details for: {}", userDetails.getUsername());
                log.info("User authorities: {}", userDetails.getAuthorities());

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authentication set successfully");
                } else {
                    log.info("Token validation failed");
                }
            } catch (UsernameNotFoundException e) {
                log.warn("User not found for username: {}", username);
            } catch (Exception e) {
                log.error("Error loading user details: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidJwtFormat(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        long periodCount = token.chars().filter(ch -> ch == '.').count();
        return periodCount == 2;
    }


}