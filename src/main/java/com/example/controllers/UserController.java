package com.example.controllers;

import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.user.UserDto;
import com.example.models.dtos.user.PasswordUpdateDto;
import com.example.models.dtos.user.RoleUpdateDto;
import com.example.models.dtos.user.UserRequestDto;
import com.example.exceptions.response.ApiResponse;
import com.example.services.UserService;
import com.example.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid UserRequestDto request) {
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

    @GetMapping("users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) throws AccessDeniedException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<UserDto> pagedUsers = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(pagedUsers));
    }

    @GetMapping("users/{id}")
   // @PreAuthorize("@securityService.canViewUser(#id)")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {
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
    public ResponseEntity<ApiResponse<String>> updatePassword(@PathVariable Long id, @Valid @RequestBody PasswordUpdateDto passwordDto) {
        return userService.updatePassword(id, passwordDto);
    }

    @PatchMapping("users/{id}/updateRole")
    @PreAuthorize("@securityService.canUpdateUserRole(#id, #roleUpdateDto.role)")
    public ResponseEntity<ApiResponse<String>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleUpdateDto roleUpdateDto) {
        return userService.updateUserRole(id, roleUpdateDto);
    }



}
