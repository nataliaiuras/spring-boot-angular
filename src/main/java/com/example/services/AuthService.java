package com.example.services;

import com.example.config.security.JWTService;
import com.example.dtos.UserDto;
import com.example.dtos.request.UserRequestDto;
import com.example.exceptions.domain.user.InvalidCredentialsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;

    public String authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(username);
            }
            throw new InvalidCredentialsException();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new InvalidCredentialsException();
        }
    }

    public Long register(UserRequestDto request) {
        return userService.register(request);
    }

    public UserDto profile(String username) {
        return userService.profile(username);
    }
}