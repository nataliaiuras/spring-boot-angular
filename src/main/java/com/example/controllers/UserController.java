package com.example.controllers;

import com.example.dtos.UserDto;
import com.example.dtos.request.PasswordUpdateDto;
import com.example.dtos.request.RoleUpdateDto;
import com.example.dtos.request.UserRequestDto;
import com.example.dtos.response.ApiResponse;
import com.example.services.UserService;
import com.example.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto request) {
        Long userId = userService.register(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userId)
                .toUri();
        return ResponseEntity
                .created(location)
                .body(ApiResponse.success("User registered successfully"));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody @Valid UserRequestDto userRequestDto) {
        String jwtToken = userService.authenticate(userRequestDto.getUsername(), userRequestDto.getPassword());
        Map<String, String> tokenData = Map.of("token", jwtToken);
        return ResponseEntity.ok(ApiResponse.success(tokenData, "Login successful"));
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully"));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<UserDto>> profile(Authentication authentication) {
        UserDto userDto = userService.profile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Set<UserDto>>> getAllUsers() throws AccessDeniedException {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @GetMapping("{id}")
    @PreAuthorize("@securityService.canViewUser(#id)")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("@securityService.canDeleteUser(#id)")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User was removed successfully"));
    }


    @PatchMapping("{id}/updatePassword")
    @PreAuthorize("@securityService.isCurrentUserOrAdmin(#id)")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDto passwordDto) {
        return userService.updatePassword(id, passwordDto);
    }

    @PatchMapping("{id}/updateRole")
    @PreAuthorize("@securityService.canUpdateUserRole(#id, #roleUpdateDto.role)")
    public ResponseEntity<ApiResponse<String>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        return userService.updateUserRole(id, roleUpdateDto);
    }



}
