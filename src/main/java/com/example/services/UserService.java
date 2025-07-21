package com.example.services;

import com.example.config.security.JWTService;
import com.example.dtos.UserProfileDto;
import com.example.dtos.request.RegisterRequestDto;
import com.example.models.User;
import com.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserService(JWTService jwtService, AuthenticationManager authManager, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElse(null);
    }

    public String register(RegisterRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return "User registered successfully";
    }

    public String verify(User user) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }

    public UserProfileDto profile(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> new UserProfileDto(value.getEmail(), value.getUsername(), value.getRole())).orElseThrow();
    }

}