package com.example.controller;

import com.example.dto.request.user.PasswordUpdateRequest;
import com.example.dto.request.user.RoleUpdateRequest;
import com.example.dto.request.user.UserCreateRequest;
import com.example.dto.response.user.UserDetailResponse;
import com.example.service.UserService;
import com.example.dto.response.general.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    /*public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }*/

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody @Valid UserCreateRequest request) {
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
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody @Valid UserCreateRequest request) {
        String jwtToken = userService.authenticate(request.getUsername(), request.getPassword());
        Map<String, String> tokenData = Map.of("token", jwtToken);
        return ResponseEntity.ok(ApiResponse.success(tokenData, "LoginComponent successful"));
    }

    @PostMapping("logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.success("User logged out successfully"));
    }

    @GetMapping("profile")
    public ResponseEntity<ApiResponse<UserDetailResponse>> profile(Authentication authentication) {
        UserDetailResponse userDetailResponse = userService.profile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(userDetailResponse));
    }

    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Page<UserDetailResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws AccessDeniedException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<UserDetailResponse> pagedUsers = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedUsers));
    }

    @GetMapping("users/{id}")
    // @PreAuthorize("@securityService.canViewUser(#id)")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @DeleteMapping("users/{id}")
    @PreAuthorize("@securityService.canDeleteUser(#id)")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User was removed successfully"));
    }


    @PatchMapping("users/{id}/updatePassword")
    @PreAuthorize("@securityService.isCurrentUserOrAdmin(#id)")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateRequest request) {
        return userService.updatePassword(id, request);
    }

    @PatchMapping("users/{id}/updateRole")
    @PreAuthorize("@securityService.canUpdateUserRole(#id, #roleUpdateDto.role)")
    public ResponseEntity<ApiResponse<String>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateRequest request) {
        return userService.updateUserRole(id, request);
    }


}
