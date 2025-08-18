package com.example.controllers;

import com.example.dtos.UserProfileDto;
import com.example.dtos.request.RegisterRequestDto;
import com.example.models.User;
import com.example.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDto request) {
        String result = userService.register(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        String jwtToken = userService.verify(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully");
    }

    @GetMapping("profile")
    public ResponseEntity<UserProfileDto> profile(Authentication authentication) {
        UserProfileDto userProfileDto = userService.profile(authentication.getName());
        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }


}
