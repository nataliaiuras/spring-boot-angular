package com.example.services;

import com.example.models.dtos.user.UserDto;
import com.example.models.dtos.user.PasswordUpdateDto;
import com.example.models.dtos.user.RoleUpdateDto;
import com.example.models.dtos.user.UserRequestDto;
import com.example.exceptions.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;
import java.util.Set;

public interface UserService {
    Long register(@Valid UserRequestDto request);

    String authenticate(@NotNull @NotEmpty String username, @NotNull @NotEmpty String password);

    UserDto profile(String name);

    Page<UserDto> getAllUsers(Pageable pageable) throws AccessDeniedException;

    UserDto getUserById(Long id);

    void deleteUser(Long id);

    ResponseEntity<?> updatePassword(Long id, @Valid PasswordUpdateDto passwordDto);

    ResponseEntity<ApiResponse<String>> updateUserRole(Long id, @Valid RoleUpdateDto roleUpdateDto);
}
