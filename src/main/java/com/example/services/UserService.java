package com.example.services;

import com.example.dtos.UserDto;
import com.example.dtos.request.PasswordUpdateDto;
import com.example.dtos.request.RoleUpdateDto;
import com.example.dtos.request.UserRequestDto;
import com.example.dtos.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Set;

public interface UserService {
    Long register(@Valid UserRequestDto request);

    String authenticate(@NotNull @NotEmpty String username, @NotNull @NotEmpty String password);

    UserDto profile(String name);

    Set<UserDto> getAllUsers() throws AccessDeniedException;

    UserDto getUserById(Long id);

    void deleteUser(Long id);

    ResponseEntity<ApiResponse<String>> updatePassword(Long id, @Valid PasswordUpdateDto passwordDto);

    ResponseEntity<ApiResponse<String>> updateUserRole(Long id, @Valid RoleUpdateDto roleUpdateDto);
}
